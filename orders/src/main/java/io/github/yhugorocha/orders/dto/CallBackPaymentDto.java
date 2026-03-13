package io.github.yhugorocha.orders.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CallBackPaymentDto {

    private Long id;
    private String paymentKey;
    private boolean status;
    private String observation;
}
