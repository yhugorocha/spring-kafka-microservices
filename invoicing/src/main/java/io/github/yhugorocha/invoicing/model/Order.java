package io.github.yhugorocha.invoicing.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record Order(
        Long id,
        Client client,
        LocalDateTime orderDate,
        BigDecimal total,
        String status,
        List<OrderItem> items
) {}