package io.github.yhugorocha.orders.dto;

import io.github.yhugorocha.orders.model.OrderItemEntity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemRequestDto {

    @NotNull(message = "productId é obrigatório")
    private Long productId;

    @NotNull(message = "quantity é obrigatório")
    @Positive(message = "quantity deve ser maior que zero")
    private Integer quantity;

    @NotNull(message = "unitPrice é obrigatório")
    @Positive(message = "unitPrice deve ser maior que zero")
    private BigDecimal unitPrice;

    public OrderItemEntity toEntity() {
        return OrderItemEntity.builder()
                .productId(this.productId)
                .quantity(this.quantity)
                .unitPrice(this.unitPrice)
                .build();
    }

    public static List<OrderItemEntity> toEntityList(List<OrderItemRequestDto> items) {
        return items.stream()
                .map(OrderItemRequestDto::toEntity)
                .toList();
    }
}
