package io.github.yhugorocha.orders.subscriber;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.yhugorocha.orders.service.OrderService;
import io.github.yhugorocha.orders.subscriber.representation.StatusUpdateLogisticsOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class OrderLogisticsSubscriber {

    private final ObjectMapper objectMapper;
    private final OrderService orderService;

    @KafkaListener(topics = "${kafka.topic.orders.shipped}", groupId = "icompras.orders")
    public void listen(String json) {
        try {
            log.info("Received order shipped event: {}", json);
            var updateOrder = objectMapper.readValue(json, StatusUpdateLogisticsOrder.class);
            Thread.sleep(5000);
            orderService.shippedOrder(updateOrder);
            log.info("Processed order shipped event for order ID: {}", updateOrder.id());
        } catch (Exception e) {
            log.error("Error processing order shipped event", e);
        }
    }
}
