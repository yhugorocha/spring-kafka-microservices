package io.github.yhugorocha.orders.client.representation;

import java.math.BigDecimal;

public record ProductRepresentation(Long id, String name, BigDecimal unitPrice) {
}
