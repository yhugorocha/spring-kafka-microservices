package io.github.yhugorocha.invoicing.subscriber;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.yhugorocha.invoicing.service.InvoiceService;
import io.github.yhugorocha.invoicing.subscriber.representantion.OrderRepresentation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class OrderPaidSubscriber {

    private final ObjectMapper objectMapper;
    private final InvoiceService invoiceService;

    @KafkaListener(topics = "${kafka.topic.orders.paid}", groupId = "icompras.invoicing")
    public void listen(String json) {
        try {
            log.info("Received order paid event: {}", json);
            var order = objectMapper.readValue(json, OrderRepresentation.class);
            invoiceService.generateInvoice(OrderRepresentation.toEntity(order));
        } catch (Exception e) {
            log.error("Error processing order paid event", e);
        }
    }
}