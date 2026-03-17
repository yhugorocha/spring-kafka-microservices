package io.github.yhugorocha.invoicing.subscriber.representantion;

import io.github.yhugorocha.invoicing.model.Client;
import io.github.yhugorocha.invoicing.model.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderRepresentation(
        Long id,
        Long clientId,
        LocalDateTime orderDate,
        String name,
        String cpf,
        String street,
        String number,
        String neighborhood,
        String email,
        String phone,
        String status,
        BigDecimal total,
        List<OrderItemRepresentation> items
) {

    public static Order toEntity (OrderRepresentation representation) {
        return new Order(
                representation.id,
                new Client(
                        representation.clientId,
                        representation.name,
                        representation.cpf,
                        representation.street,
                        representation.number,
                        representation.neighborhood,
                        representation.email,
                        representation.phone
                ),
                representation.orderDate,
                representation.total,
                representation.status,
                OrderItemRepresentation.toEntityList(representation.items)
        );
    }
}