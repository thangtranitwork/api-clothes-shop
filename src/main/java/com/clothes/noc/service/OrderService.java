package com.clothes.noc.service;

import com.clothes.noc.dto.request.OrderRequest;
import com.clothes.noc.dto.request.SearchOrderRequest;
import com.clothes.noc.dto.response.CreateOrderResponse;
import com.clothes.noc.dto.response.OrderResponse;
import com.clothes.noc.entity.*;
import com.clothes.noc.enums.OrderStatus;
import com.clothes.noc.enums.PaymentType;
import com.clothes.noc.exception.AppException;
import com.clothes.noc.exception.ErrorCode;
import com.clothes.noc.mapper.OrderMapper;
import com.clothes.noc.repository.OrderRepository;
import com.clothes.noc.repository.ProductVariantRepository;
import com.clothes.noc.repository.spec.OrderSpecifications;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final OrderMapper orderMapper;
    private final CartService cartService;
    private final ProductVariantRepository productVariantRepository;
    private final EmailService emailService;
    private final VNPayService vnPayService;

    @Value("${FE_ORIGIN}")
    private String feOrigin;

    public OrderResponse getOrder(String id) {
        return orderMapper.toOrderResponse(orderRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_EXIST)));
    }

    public Page<OrderResponse> getHistory(SearchOrderRequest request, Pageable pageable) {
        request.setUserId(userService.getCurrentUserId());
        return orderRepository.findAll(OrderSpecifications.multipleFieldsSearch(request), pageable)
                .map(orderMapper::toOrderResponse);
     }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public CreateOrderResponse createOrder(OrderRequest orderRequest, HttpServletRequest request) {
        Order order = orderMapper.toOrder(orderRequest);
        List<OrderItem> orderItems = mapCartItemToOrderItem(order);
        order.setItems(orderItems);
        order.setUser(userService.getCurrentUser());
        Payment payment = getPayment(orderRequest);
        payment.setOrder(order);
        order.setPayment(payment);
        orderRepository.save(order);
        cartService.clearCart();
        OrderResponse orderResponse = orderMapper.toOrderResponse(order);
        if (payment.getType().equals(PaymentType.VNPAY)) {
            int total = order.getItems().stream()
                    .mapToInt(item ->
                            item.getPrice() * item.getQuantity()).sum();
            orderResponse.setPayUrl(createVNPAYOrderUrl(orderResponse.getId(), total, request));
        }
        sendOrderEmail(orderResponse);
        return CreateOrderResponse.builder()
                .id(orderResponse.getId())
                .payUrl(orderResponse.getPayUrl())
                .build();
    }

    public String createVNPAYOrderUrl(String orderId, int total, HttpServletRequest request) {

        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();

        return vnPayService.createOrder(request, total, orderId, baseUrl);
    }

    private void sendOrderEmail(OrderResponse orderResponse) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("order", orderResponse);
        variables.put("orderLink", String.format("%s/order/%s", feOrigin, orderResponse.getId()));
        String template = "order-email";
        String subject = "Order";
        emailService.sendMail(userService.getCurrentUser().getEmail(), subject, variables, template);
    }

    private static Payment getPayment(OrderRequest orderRequest) {
        Payment payment = new Payment();
        payment.setType(PaymentType.valueOf(orderRequest.getPaymentMethod()));
        return payment;
    }
    private List<OrderItem> mapCartItemToOrderItem(Order order) {
        List<CartItem> cartItems = userService.getCurrentUser().getCart().getItems();
        if (cartItems.isEmpty())
            throw new AppException(ErrorCode.CART_IS_EMPTY);
        Map<String, Integer> invalidQuantity = new HashMap<>();
        List<OrderItem> orderItems = cartItems.stream().map(ci -> {
            if (ci.getQuantity() > ci.getProductVariant().getQuantity() || ci.getProductVariant().getQuantity() == 0) {
                invalidQuantity.put(ci.getProductVariant().getId(), ci.getProductVariant().getQuantity());
            } else {
                ProductVariant productVariant = ci.getProductVariant();
                productVariant.setQuantity(productVariant.getQuantity() - ci.getQuantity());
                productVariantRepository.save(productVariant);
            }
            return OrderItem.builder()
                    .order(order)
                    .productVariant(ci.getProductVariant())
                    .price(ci.getProductVariant().getProduct().getPrice())
                    .quantity(ci.getQuantity())
                    .build();
        }).toList();

        if (!invalidQuantity.isEmpty())
            throw new AppException(ErrorCode.SOME_ITEMS_HAVE_INVALID_QUANTITY);
        return orderItems;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void cancelOrder(String orderId) {
        orderRepository.findById(orderId)
                .ifPresentOrElse(
                        order -> {
                            if(!userService.getCurrentUserId().equals(order.getUser().getId())) {
                                throw new AppException(ErrorCode.ACCESS_DENIED);
                            }

                            if ((order.getPayment().getType().equals(PaymentType.VNPAY) && order.getPayment().getPayTime() != null) || order.getPayment().getType().equals(PaymentType.CASH) && !order.getStatus().canTransitionTo(OrderStatus.CANCELLED)) {
                                throw new AppException(ErrorCode.CAN_NOT_CANCEL_THIS_ORDER);
                            }
                            order.getItems().forEach(item -> {
                                ProductVariant productVariant = item.getProductVariant();
                                productVariant.setQuantity(productVariant.getQuantity() + item.getQuantity());
                                productVariantRepository.save(productVariant);
                            });

                            order.setStatus(OrderStatus.CANCELLED);
                            orderRepository.save(order);
                        },
                        () -> {
                            throw new AppException(ErrorCode.ORDER_NOT_EXIST);
                        });

    }

    @Transactional
    public void handlePayment(HttpServletRequest req, HttpServletResponse res) throws IOException {
        int paymentStatus = vnPayService.orderReturn(req);
        String orderId = req.getParameter("vnp_OrderInfo");
        String paymentTime = req.getParameter("vnp_PayDate");
        String transactionId = req.getParameter("vnp_TransactionNo");//cần lưu vào db để truy xuất

        if (paymentStatus == 1) {
            orderRepository.findById(orderId).ifPresent(order -> {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
                LocalDateTime localDateTime = LocalDateTime.parse(paymentTime, formatter);
                order.getPayment().setPayTime(localDateTime);
                order.getPayment().setTransactionId(transactionId);
                orderRepository.save(order);
                try {
                    res.sendRedirect(String.format("%s/order/%s/?payment-status=success", feOrigin, orderId));
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            });

        } else {
            res.sendRedirect(String.format("%s/order/%s/?payment-status=fail", feOrigin, orderId));
        }
    }

    @Transactional
    public String payAgain(String id, HttpServletRequest req) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_EXIST));

        if (!userService.getCurrentUserId().equals(order.getUser().getId())) {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }

        if (!order.getPayment().getType().equals(PaymentType.VNPAY) || order.getPayment().getPayTime() != null) {
            throw new AppException(ErrorCode.CAN_NOT_PAY_THIS_ORDER);
        }

        int total = order.getItems().stream()
                .mapToInt(item ->
                        item.getPrice() * item.getQuantity()).sum();
        return createVNPAYOrderUrl(order.getId(), total, req);
    }
}
