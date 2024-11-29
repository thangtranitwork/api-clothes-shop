package com.clothes.noc.service;

import com.clothes.noc.dto.request.OrderRequest;
import com.clothes.noc.dto.response.CreateOrderResponse;
import com.clothes.noc.dto.response.OrderResponse;
import com.clothes.noc.entity.*;
import com.clothes.noc.exception.AppException;
import com.clothes.noc.exception.ErrorCode;
import com.clothes.noc.mapper.OrderMapper;
import com.clothes.noc.repository.OrderRepository;
import com.clothes.noc.repository.ProductVariantRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final OrderMapper orderMapper;
    private final CartService cartService;
    private final ProductVariantRepository productVariantRepository;
    private final EmailService emailService;
    @NonFinal
    private final List<OrderStatus> canCancelStatus = new ArrayList<>(Arrays.asList(OrderStatus.NEW, OrderStatus.PACKING));
    @NonFinal
    private final String ORDER_TEMPLATE = "order-email";
    @NonFinal
    private final String ORDER_SUBJECT = "Order";
    @Value("${FE_ORIGIN}")
    private String feOrigin;

    public OrderResponse getOrder(String id) {
        return orderMapper.toOrderResponse(orderRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_EXIST)));
    }

    public Page<OrderResponse> getHistory(Pageable pageable) {
        return orderRepository.findAllByUserId(userService.getCurrentUserId(), pageable).map(orderMapper::toOrderResponse);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public CreateOrderResponse createOrder(OrderRequest orderRequest) {
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
            orderResponse.setPayUrl("abc.com");
            //TODO: call VNPAY service here
        }
        sendOrderEmail(orderResponse);
        return CreateOrderResponse.builder()
                .id(orderResponse.getId())
                .payUrl(orderResponse.getPayUrl())
                .build();
    }
    private void sendOrderEmail(OrderResponse orderResponse) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("order", orderResponse);
        variables.put("orderLink", String.format("%s/order/%s", feOrigin, orderResponse.getId()));
        emailService.sendMail(userService.getCurrentUser().getEmail(), ORDER_SUBJECT, variables, ORDER_TEMPLATE);
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
        List<OrderItem> orderItems = cartItems.stream().map((ci) -> {
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
                        (order) -> {
                            if(!userService.getCurrentUserId().equals(order.getUser().getId())) {
                                throw new AppException(ErrorCode.ACCESS_DENIED);
                            }

                            if((order.getPayment().getType().equals(PaymentType.VNPAY) && order.getPayment().getPayTime() != null) || order.getPayment().getType().equals(PaymentType.CASH) && !canCancelStatus.contains(order.getStatus())) {
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


}
