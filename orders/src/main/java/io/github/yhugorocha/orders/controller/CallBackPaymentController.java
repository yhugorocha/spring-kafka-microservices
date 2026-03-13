package io.github.yhugorocha.orders.controller;

import io.github.yhugorocha.orders.dto.CallBackPaymentDto;
import io.github.yhugorocha.orders.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/callback-payment")
public class CallBackPaymentController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Void> callbackPayment(@RequestBody CallBackPaymentDto callBackPaymentDto,
                                          @RequestHeader(required = true, name = "apiKey") String apiKey) {
        orderService.processPaymentCallback(callBackPaymentDto, apiKey);
        return ResponseEntity.ok().build();
    }
}
