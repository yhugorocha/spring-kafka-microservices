package io.github.yhugorocha.invoicing.publisher.representation;

public record StatusUpdateOrder(Long id, OrderStatus orderStatus, String invoiceUrl) {
}