package io.github.yhugorocha.orders.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.yhugorocha.orders.publisher.representantion.OrderRepresentation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentPublisher {

    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;
    @Value("${kafka.topic.orders.paid}")
    private String topic;

    public void publisher(OrderRepresentation orderRepresentation){
        try {
            String json = objectMapper.writeValueAsString(orderRepresentation);
            log.info("Publishing payment message: {}", json);
            kafkaTemplate.send(topic, json);
        } catch (JsonProcessingException e) {
            log.error("Error processing JSON", e);
        } catch (RuntimeException e) {
            log.error("Error send message", e);
        }
    }
}
