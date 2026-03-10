package io.github.yhugorocha.clients.dto;

import io.github.yhugorocha.clients.entity.Client;

public record ClientResponse(
        Long id,
        String name,
        String cpf,
        String street,
        String number,
        String neighborhood,
        String email,
        String phone
) {

    public static ClientResponse fromEntity(Client client) {
        return new ClientResponse(
                client.getId(),
                client.getName(),
                client.getCpf(),
                client.getStreet(),
                client.getNumber(),
                client.getNeighborhood(),
                client.getEmail(),
                client.getPhone()
        );
    }
}
