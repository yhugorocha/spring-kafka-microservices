package io.github.yhugorocha.orders.client.representation;

public record ClientRepresentation(Long id,
                                   String name,
                                   String cpf,
                                   String street,
                                   String number,
                                   String neighborhood,
                                   String email,
                                   String phone) {
}
