package io.github.yhugorocha.orders.publisher.representation;

import java.math.BigDecimal;

public record OrderItemRepresentation(
        Long id,
        String name,
        Integer quantity,
        BigDecimal unitPrice
) {
    public BigDecimal getTotal(){
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}