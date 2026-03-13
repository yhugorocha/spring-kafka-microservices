package io.github.yhugorocha.orders.client;

import io.github.yhugorocha.orders.model.OrderEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class BankingServiceClient {

    public String RequestPayment(OrderEntity orderEntity){
        log.info("Solicitando pagamento para o pedido: {}", orderEntity.getId());
        return UUID.randomUUID().toString();
    }
}
