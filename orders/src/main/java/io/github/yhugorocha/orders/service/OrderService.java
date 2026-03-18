package io.github.yhugorocha.orders.service;

import io.github.yhugorocha.orders.dto.*;
import io.github.yhugorocha.orders.publisher.representation.OrderRepresentation;
import io.github.yhugorocha.orders.subscriber.representation.StatusUpdateInvoiceOrder;
import io.github.yhugorocha.orders.subscriber.representation.StatusUpdateLogisticsOrder;

public interface OrderService {

    OrderResponseDto create(OrderCreateRequestDto request);

    void processPaymentCallback(CallBackPaymentDto callBackPaymentDto, String apiKey);

    OrderResponseDto addNewPayment(Long id, PaymentDetailsRequestDto request);

    OrderRepresentation orderInformationById(Long id);

    void invoiceOrder(StatusUpdateInvoiceOrder updateOrder);

    void shippedOrder (StatusUpdateLogisticsOrder updateOrder);
}
