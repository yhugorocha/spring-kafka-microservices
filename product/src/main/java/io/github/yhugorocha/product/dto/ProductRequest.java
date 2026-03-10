package io.github.yhugorocha.product.dto;

import io.github.yhugorocha.product.model.Product;

import java.math.BigDecimal;

public record ProductRequest(String name, BigDecimal unitPrice) {

    public Product toEntity() {
        return Product.builder()
                .name(name)
                .unitPrice(unitPrice)
                .build();
    }
}
