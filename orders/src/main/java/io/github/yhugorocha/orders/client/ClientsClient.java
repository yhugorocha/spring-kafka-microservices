package io.github.yhugorocha.orders.client;

import io.github.yhugorocha.orders.client.representation.ClientRepresentation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "clients", url = "${icompras.order.client.clients.url}")
public interface ClientsClient {

    @GetMapping("/{id}")
    ResponseEntity<ClientRepresentation> findById(@PathVariable Long id);

}
