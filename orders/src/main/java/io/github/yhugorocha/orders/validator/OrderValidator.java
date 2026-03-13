package io.github.yhugorocha.orders.validator;

import io.github.yhugorocha.orders.client.ClientsClient;
import io.github.yhugorocha.orders.client.ProductClient;
import io.github.yhugorocha.orders.model.OrderEntity;
import io.github.yhugorocha.orders.model.OrderItemEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Slf4j
@AllArgsConstructor
@Component
public class OrderValidator {

    private final ProductClient productClient;
    private final ClientsClient clientsClient;
    private final Executor orderValidationExecutor;

    public void validateOrder(OrderEntity orderEntity) {
        var productsFuture = CompletableFuture.runAsync(() -> validateProducts(orderEntity), orderValidationExecutor);
        var clientFuture = CompletableFuture.runAsync(() -> validateClient(orderEntity.getClientId()), orderValidationExecutor);

        CompletableFuture.allOf(productsFuture, clientFuture).join();
    }

    public void validateProducts(OrderEntity orderEntity) {
        var list = orderEntity.getItems().stream()
                .map(OrderItemEntity::getProductId)
                .toList();

        this.findProductById(list);
    }

    private void findProductById(List<Long> ids) {
        var response = productClient.findAllById(ids);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new IllegalArgumentException("Um ou mais produtos não encontrados");
        }
        log.info("Todos os produtos encontrados: {}", response.getBody());
    }

    public void validateClient(Long id){
        var response = clientsClient.findById(id);

        if(!response.getStatusCode().is2xxSuccessful()){
            throw new IllegalArgumentException("Cliente com id " + id + " não encontrado");
        }
        log.info("Cliente com id {} encontrado", id);
    }
}
