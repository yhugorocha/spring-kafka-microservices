package io.github.yhugorocha.logistics.subscriber;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.yhugorocha.logistics.service.LogisticsService;
import io.github.yhugorocha.logistics.subscriber.representation.StatusUpdateInvoice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class OrderLogisticsSubscriber {

    private final ObjectMapper objectMapper;
    private final LogisticsService logisticsService;

    @KafkaListener(topics = "${kafka.topic.orders.invoiced}", groupId = "icompras.logistics")
    public void listen(String json) {
        try {
            log.info("Received order logistics event: {}", json);
            var updateOrder = objectMapper.readValue(json, StatusUpdateInvoice.class);
            logisticsService.generateTrackingNumber(updateOrder);
            log.info("Processed order logistics event for order ID: {}", updateOrder.id());
        } catch (Exception e) {
            log.error("Error processing order logistics event", e);
        }
    }
}
