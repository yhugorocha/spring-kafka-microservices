package io.github.yhugorocha.orders.dto;

import io.github.yhugorocha.orders.model.OrderItemEntity;
import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponseDto {

    private Long id;
    private Long productId;
    private Integer quantity;
    private BigDecimal unitPrice;

    public static OrderItemResponseDto fromEntity(OrderItemEntity entity) {
        return OrderItemResponseDto.builder()
                .id(entity.getId())
                .productId(entity.getProductId())
                .quantity(entity.getQuantity())
                .unitPrice(entity.getUnitPrice())
                .build();
    }

    public static List<OrderItemResponseDto> fromEntityList(List<OrderItemEntity> items) {
        return items.stream()
                .map(OrderItemResponseDto::fromEntity)
                .toList();
    }
}
