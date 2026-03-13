package io.github.yhugorocha.orders.client;

import io.github.yhugorocha.orders.client.representation.ProductRepresentation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "product", url = "${icompras.order.client.products.url}")
public interface ProductClient {

    @GetMapping("/validate")
    ResponseEntity<List<ProductRepresentation>> findAllById(@RequestParam List<Long> ids);

}
