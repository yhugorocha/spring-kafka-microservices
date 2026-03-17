package io.github.yhugorocha.invoicing.subscriber.representation;

import io.github.yhugorocha.invoicing.model.OrderItem;

import java.math.BigDecimal;
import java.util.List;

public record OrderItemRepresentation(
        Long id,
        String name,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal total
) {
    public static OrderItem toEntity(OrderItemRepresentation representation){
        return new OrderItem(
                representation.id,
                representation.name,
                representation.quantity,
                representation.unitPrice,
                representation.total
        );
    }

    public static List<OrderItem> toEntityList(List<OrderItemRepresentation> orderItemRepresentation){
        return orderItemRepresentation.stream()
                .map(OrderItemRepresentation::toEntity)
                .toList();
    }
}