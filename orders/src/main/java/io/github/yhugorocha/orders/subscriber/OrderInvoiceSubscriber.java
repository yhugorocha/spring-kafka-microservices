package io.github.yhugorocha.orders.subscriber;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.yhugorocha.orders.service.OrderService;
import io.github.yhugorocha.orders.subscriber.representation.StatusUpdateOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class OrderInvoiceSubscriber {

    private final ObjectMapper objectMapper;
    private final OrderService orderService;

    @KafkaListener(topics = "${kafka.topic.orders.invoiced}", groupId = "icompras.orders")
    public void listen(String json) {
        try {
            log.info("Received order invoiced event: {}", json);
            var updateOrder = objectMapper.readValue(json, StatusUpdateOrder.class);
            orderService.invoiceOrder(updateOrder);
            log.info("Processed order invoiced event for order ID: {}", updateOrder.id());
        } catch (Exception e) {
            log.error("Error processing order invoiced event", e);
        }
    }
}
