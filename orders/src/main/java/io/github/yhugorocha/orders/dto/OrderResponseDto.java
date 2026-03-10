package io.github.yhugorocha.orders.dto;

import io.github.yhugorocha.orders.model.OrderEntity;
import io.github.yhugorocha.orders.model.enums.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto {

    private Long id;
    private Long clientId;
    private LocalDateTime orderDate;
    private String paymentKey;
    private String notes;
    private OrderStatus status;
    private BigDecimal total;
    private String trackingCode;
    private String invoiceUrl;
    private List<OrderItemResponseDto> items;

    public static OrderResponseDto fromEntity(OrderEntity entity) {
        return OrderResponseDto.builder()
                .id(entity.getId())
                .clientId(entity.getClientId())
                .orderDate(entity.getOrderDate())
                .paymentKey(entity.getPaymentKey())
                .notes(entity.getObservations())
                .status(entity.getStatus())
                .total(entity.getTotal())
                .trackingCode(entity.getTrackingCode())
                .invoiceUrl(entity.getInvoiceUrl())
                .items(OrderItemResponseDto.fromEntityList(entity.getItems()))
                .build();
    }
}
