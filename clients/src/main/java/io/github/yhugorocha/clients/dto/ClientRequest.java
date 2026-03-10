package io.github.yhugorocha.clients.dto;

public record ClientRequest(
        String name,
        String cpf,
        String street,
        String number,
        String neighborhood,
        String email,
        String phone
) {
}
