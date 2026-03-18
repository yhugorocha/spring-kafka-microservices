package io.github.yhugorocha.orders.subscriber.representation;

import io.github.yhugorocha.orders.model.enums.OrderStatus;

public record StatusUpdateLogisticsOrder(Long id, OrderStatus orderStatus, String trackingNumber) {
}