package io.github.yhugorocha.invoicing.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record Order(
        Long id,
        Long clientId,
        LocalDateTime orderDate,
        String name,
        String cpf,
        String street,
        String number,
        String neighborhood,
        String email,
        String phoneNumber,
        BigDecimal total,
        List<OrderItem> items
) {}