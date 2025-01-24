package com.clothes.noc.admin.service;

import com.clothes.noc.dto.request.SearchOrderRequest;
import com.clothes.noc.dto.response.OrderWithUserResponse;
import com.clothes.noc.dto.response.SummaryResponse;
import com.clothes.noc.entity.Order;
import com.clothes.noc.entity.ProductVariant;
import com.clothes.noc.enums.OrderStatus;
import com.clothes.noc.enums.SummaryType;
import com.clothes.noc.exception.AppException;
import com.clothes.noc.exception.ErrorCode;
import com.clothes.noc.mapper.OrderMapper;
import com.clothes.noc.repository.OrderRepository;
import com.clothes.noc.repository.ProductVariantRepository;
import com.clothes.noc.repository.spec.OrderSpecifications;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class OrderManageService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ProductVariantRepository productVariantRepository;

    @Transactional
    public Page<OrderWithUserResponse> getAll(SearchOrderRequest request, Pageable pageable) {
        return orderRepository.findAll(OrderSpecifications.multipleFieldsSearch(request), pageable)
                .map(orderMapper::toOrderWithUserResponse);
    }

    public Order get(String id) {
        return orderRepository.findById(id)
                .orElseThrow(() ->
                        new AppException(ErrorCode.ORDER_NOT_FOUND));
    }

    public SummaryResponse generateSummary(SummaryType option, int num) {
        SummaryResponse response = SummaryResponse.builder().build();
        response.setData(new ArrayList<>());
        if (SummaryType.DAY.equals(option)) {
            List<String> labels = IntStream.rangeClosed(0, 23)
                    .mapToObj(String::valueOf)
                    .toList();
            response.setLabels(labels);
            for (int i = 0; i < num; i++) {
                int[] data = new int[24];
                LocalDate date = LocalDate.now().minusDays(i);
                orderRepository.summaryByDay(date).forEach(o -> {
                    int hour = (int) o[0];         // Lấy giờ từ kết quả query
                    int total = ((BigDecimal) o[1]).intValue(); // Lấy tổng từ kết quả query
                    data[hour] = total;  // Đưa tổng vào vị trí tương ứng với giờ
                });
                response.getData().add(Map.of(String.format("%d-%d-%d", date.getDayOfYear(), date.getMonthValue(), date.getYear()), Arrays.stream(data).boxed().toList()));
            }
        } else if (SummaryType.WEEK.equals(option)) {
            List<String> labels = Arrays.asList("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");
            LocalDate date = LocalDate.now();
            int week = date.get(WeekFields.of(Locale.getDefault()).weekOfYear());
            int year = date.getYear();
            for (int i = 0; i < num; i++, week--) {
                int[] data = new int[7];
                if (week == 0) {
                    year--;
                    week = 53;
                }
                orderRepository.summaryByWeek(year, week).forEach(o -> {
                    int day = (int) o[0]; // Lấy ngày trong tuần (1 = Sunday, 2 = Monday, ...)
                    int total = ((BigDecimal) o[1]).intValue(); // Lấy tổng số tiền
                    data[day - 1] = total; // Gán tổng vào mảng current (day-1 vì mảng bắt đầu từ 0)
                });
                response.getData().add(Map.of(String.format("%d-%d", week, year), Arrays.stream(data).boxed().toList()));
            }
            response.setLabels(labels);
        } else if (SummaryType.MONTH.equals(option)) {
            // Tạo labels cho các ngày trong tháng (1 đến 31)
            List<String> labels = IntStream.rangeClosed(1, LocalDate.now().lengthOfMonth()) // Tạo số lượng ngày trong tháng
                    .mapToObj(String::valueOf)
                    .toList();
            for (int i = 0; i < num; i++) {
                LocalDate date = LocalDate.now().minusMonths(i);
                int[] data = new int[date.lengthOfMonth()];
                int month = date.getMonthValue();
                int year = date.getYear();
                orderRepository.summaryByMonth(year, month).forEach(o -> {
                    int day = (int) o[0]; // Lấy ngày trong tuần (1 = Sunday, 2 = Monday, ...)
                    int total = ((BigDecimal) o[1]).intValue(); // Lấy tổng số tiền
                    data[day - 1] = total; // Gán tổng vào mảng current (day-1 vì mảng bắt đầu từ 0)
                });
                response.getData().add(Map.of(String.format("%d-%d", month, year), Arrays.stream(data).boxed().toList()));
            }

            response.setLabels(labels);
        } else if (SummaryType.YEAR.equals(option)) {
            // Tạo labels cho các tháng trong năm
            List<String> labels = Arrays.asList("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec");
            for (int i = 0; i < num; i++) {
                int[] data = new int[12];
                LocalDate date = LocalDate.now().minusYears(i);
                int year = date.getYear();
                orderRepository.summaryByYear(year).forEach(o -> {
                    int day = (int) o[0]; // Lấy ngày trong tuần (1 = Sunday, 2 = Monday, ...)
                    int total = ((BigDecimal) o[1]).intValue(); // Lấy tổng số tiền
                    data[day - 1] = total; // Gán tổng vào mảng current (day-1 vì mảng bắt đầu từ 0)
                });
                response.getData().add(Map.of(String.format("%d", year), Arrays.stream(data).boxed().toList()));
            }
            // Thiết lập labels và dữ liệu cho phản hồi
            response.setLabels(labels);
        }
        return response;
    }

    public SummaryResponse generateSummaryByCategory() {
        List<String> labels = new ArrayList<>();
        List<Integer> current = new ArrayList<>();
        orderRepository.summaryByCategory().forEach(o -> {
            labels.add((String) o[0]);
            int total = ((BigDecimal) o[1]).intValue();
            current.add((total));
        });
        return SummaryResponse.builder()
                .labels(labels)
                .current(current)
                .build();
    }

    @Transactional
    public void updateOrderStatus(String id, OrderStatus newStatus) {
        OrderStatus status = orderRepository.getOrderStatus(id)
                .orElseThrow(() ->
                        new AppException(ErrorCode.ORDER_NOT_FOUND));
        if (status.canTransitionTo(newStatus)) {
            if(OrderStatus.SUCCESS.equals(newStatus)) {
                orderRepository.updateOrderStatusAndPayTime(id, newStatus, LocalDateTime.now());
            }
            else if(OrderStatus.REJECTED.equals(newStatus) || OrderStatus.FAILED.equals(newStatus)) {
                Order order = get(id);
                order.getItems().forEach(item -> {
                    ProductVariant productVariant = item.getProductVariant();
                    productVariant.setQuantity(productVariant.getQuantity() + item.getQuantity());
                    productVariantRepository.save(productVariant);
                });
                order.setStatus(newStatus);
                orderRepository.save(order);
            }
            else{
                orderRepository.updateOrderStatus(id, newStatus);
            }
        } else {
            throw new AppException(
                    ErrorCode.UPDATE_STATUS_FAILED,
                    Map.of("from", status.name(), "to", newStatus.name())
            );
        }
    }

}
