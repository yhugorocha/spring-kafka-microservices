package io.github.yhugorocha.orders.dto;

import io.github.yhugorocha.orders.model.enums.PaymentType;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDetailsRequestDto {

    @NotEmpty(message = "details é obrigatório")
    private String details;

    @NotEmpty(message = "paymentType é obrigatório")
    private PaymentType paymentType;
}
