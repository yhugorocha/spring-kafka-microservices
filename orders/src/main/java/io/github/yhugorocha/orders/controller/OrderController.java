package io.github.yhugorocha.orders.controller;

import io.github.yhugorocha.orders.dto.OrderCreateRequestDto;
import io.github.yhugorocha.orders.dto.OrderResponseDto;
import io.github.yhugorocha.orders.dto.PaymentDetailsRequestDto;
import io.github.yhugorocha.orders.publisher.representation.OrderRepresentation;
import io.github.yhugorocha.orders.service.OrderService;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/orders")
@AllArgsConstructor
public class OrderController {

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

    @PatchMapping("/new-payment/{id}")
    public ResponseEntity<OrderResponseDto> updatePayment(@PathVariable("id") Long id,
                                                          @Valid @RequestBody PaymentDetailsRequestDto request) {
        return ResponseEntity.ok(orderService.addNewPayment(id, request));
    }

    @GetMapping("/information/{id}")
    public ResponseEntity<OrderRepresentation> orderInformation(@PathVariable("id") Long id){
        return ResponseEntity.ok(orderService.orderInformationById(id));
    }
}