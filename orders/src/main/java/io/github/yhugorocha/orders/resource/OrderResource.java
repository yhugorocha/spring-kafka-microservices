package io.github.yhugorocha.orders.resource;

import io.github.yhugorocha.orders.dto.OrderCreateRequestDto;
import io.github.yhugorocha.orders.dto.OrderResponseDto;
import io.github.yhugorocha.orders.dto.OrderStatusUpdateRequestDto;
import io.github.yhugorocha.orders.service.OrderService;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/orders")
@AllArgsConstructor
public class OrderResource {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponseDto> create(@Valid @RequestBody OrderCreateRequestDto request) {
        var createdOrder = orderService.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdOrder.getId())
                .toUri();

        return ResponseEntity.created(location).body(createdOrder);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderResponseDto> updateStatus(@PathVariable("id") Long id,
                                                         @Valid @RequestBody OrderStatusUpdateRequestDto request) {
        return ResponseEntity.ok(orderService.updateStatus(id, request));
    }
}
