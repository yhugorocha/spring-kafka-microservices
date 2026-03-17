package io.github.yhugorocha.orders.service;

import io.github.yhugorocha.orders.dto.*;
import io.github.yhugorocha.orders.publisher.representation.OrderRepresentation;
import io.github.yhugorocha.orders.subscriber.representation.StatusUpdateOrder;

public interface OrderService {

    OrderResponseDto create(OrderCreateRequestDto request);

    void processPaymentCallback(CallBackPaymentDto callBackPaymentDto, String apiKey);

    OrderResponseDto addNewPayment(Long id, PaymentDetailsRequestDto request);

    OrderRepresentation orderInformationById(Long id);

    void invoiceOrder(StatusUpdateOrder updateOrder);
}
