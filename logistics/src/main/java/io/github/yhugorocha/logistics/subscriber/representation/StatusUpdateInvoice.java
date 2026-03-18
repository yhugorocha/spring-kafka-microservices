package io.github.yhugorocha.logistics.subscriber.representation;

public record StatusUpdateInvoice(Long id, OrderStatus orderStatus, String invoiceUrl) {
}