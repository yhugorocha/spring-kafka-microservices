package io.github.yhugorocha.product.dto;

import io.github.yhugorocha.product.model.Product;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ProductResponse(Long id, String name, BigDecimal unitPrice) {

    public static ProductResponse fromEntity(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .unitPrice(product.getUnitPrice())
                .build();
    }
}
