package io.github.yhugorocha.orders.service;

import io.github.yhugorocha.orders.dto.*;
import io.github.yhugorocha.orders.publisher.representantion.OrderRepresentation;

public interface OrderService {

    OrderResponseDto create(OrderCreateRequestDto request);

    void processPaymentCallback(CallBackPaymentDto callBackPaymentDto, String apiKey);

    OrderResponseDto addNewPayment(Long id, PaymentDetailsRequestDto request);

    OrderRepresentation orderInformationById(Long id);
}
