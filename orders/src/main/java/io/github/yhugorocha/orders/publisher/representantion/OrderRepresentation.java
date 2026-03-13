package io.github.yhugorocha.orders.publisher.representantion;

import io.github.yhugorocha.orders.client.representation.ClientRepresentation;
import io.github.yhugorocha.orders.model.OrderEntity;
import io.github.yhugorocha.orders.model.enums.OrderStatus;

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
        String phoneNumber,
        BigDecimal total,
        OrderStatus status,
        List<OrderItemRepresentation> items
) {

    public static OrderRepresentation toEntity (OrderEntity orderEntity,
                                                    ClientRepresentation clientRepresentation,
                                                    List<OrderItemRepresentation> items) {
        return new OrderRepresentation(
                orderEntity.getId(),
                orderEntity.getClientId(),
                orderEntity.getOrderDate(),
                clientRepresentation.name(),
                clientRepresentation.cpf(),
                clientRepresentation.street(),
                clientRepresentation.number(),
                clientRepresentation.neighborhood(),
                clientRepresentation.email(),
                clientRepresentation.phoneNumber(),
                orderEntity.getTotal(),
                orderEntity.getStatus(),
                items
        );
    }
}