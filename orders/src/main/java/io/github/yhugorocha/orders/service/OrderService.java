package io.github.yhugorocha.orders.service;

import io.github.yhugorocha.orders.dto.CallBackPaymentDto;
import io.github.yhugorocha.orders.dto.OrderCreateRequestDto;
import io.github.yhugorocha.orders.dto.OrderResponseDto;
import io.github.yhugorocha.orders.dto.OrderStatusUpdateRequestDto;

public interface OrderService {

    OrderResponseDto create(OrderCreateRequestDto request);

    OrderResponseDto updateStatus(Long orderId, OrderStatusUpdateRequestDto request);

    void processPaymentCallback(CallBackPaymentDto callBackPaymentDto, String apiKey);
}
