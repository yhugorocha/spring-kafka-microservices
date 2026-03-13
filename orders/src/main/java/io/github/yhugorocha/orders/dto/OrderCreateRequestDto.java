package io.github.yhugorocha.orders.dto;

import io.github.yhugorocha.orders.model.OrderEntity;
import io.github.yhugorocha.orders.model.enums.PaymentType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateRequestDto {

    @NotNull(message = "clientId é obrigatório")
    private Long clientId;

    @NotNull
    private PaymentDetailsRequestDto paymentDetails;

    @Valid
    @NotEmpty(message = "items deve ter pelo menos um item")
    private List<OrderItemRequestDto> items;

    public OrderEntity toEntity() {
        return OrderEntity.builder()
                .clientId(this.clientId)
                .details(this.paymentDetails.getDetails())
                .paymentType(PaymentType.valueOf(this.paymentDetails.getPaymentType()))
                .build();
    }
}
