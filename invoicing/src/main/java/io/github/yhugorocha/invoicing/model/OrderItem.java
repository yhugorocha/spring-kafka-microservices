package io.github.yhugorocha.invoicing.model;

import java.math.BigDecimal;

public record OrderItem(
        Long id,
        String name,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal total
) {}