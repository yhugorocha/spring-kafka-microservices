package io.github.yhugorocha.orders.publisher.representantion;

import io.github.yhugorocha.orders.model.OrderItemEntity;

import java.math.BigDecimal;
import java.util.List;

public record OrderItemRepresentation(
        Long id,
        String name,
        Integer quantity,
        BigDecimal unitPrice
) {
    public BigDecimal getTotal(){
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    public static OrderItemRepresentation toEntity(OrderItemEntity orderItemEntity){
        return new OrderItemRepresentation(
                orderItemEntity.getId(),
                "name",
                orderItemEntity.getQuantity(),
                orderItemEntity.getUnitPrice()
        );
    }

    public static List<OrderItemRepresentation> toEntityList(List<OrderItemEntity> orderItemEntities){
        return orderItemEntities.stream()
                .map(OrderItemRepresentation::toEntity)
                .toList();
    }
}