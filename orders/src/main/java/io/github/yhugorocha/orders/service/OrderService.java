package io.github.yhugorocha.orders.service;

import io.github.yhugorocha.orders.dto.*;

public interface OrderService {

    OrderResponseDto create(OrderCreateRequestDto request);

    void processPaymentCallback(CallBackPaymentDto callBackPaymentDto, String apiKey);

    OrderResponseDto addNewPayment(Long id, PaymentDetailsRequestDto request);
}
