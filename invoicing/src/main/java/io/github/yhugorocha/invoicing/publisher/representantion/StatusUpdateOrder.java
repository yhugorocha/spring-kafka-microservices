package io.github.yhugorocha.invoicing.publisher.representantion;

public record StatusUpdateOrder(Long id, OrderStatus orderStatus, String invoiceUrl) {
}