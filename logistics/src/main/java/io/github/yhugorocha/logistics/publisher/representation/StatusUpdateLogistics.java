package io.github.yhugorocha.logistics.publisher.representation;

import io.github.yhugorocha.logistics.subscriber.representation.OrderStatus;

public record StatusUpdateLogistics(Long id, OrderStatus orderStatus, String trackingNumber) {
}