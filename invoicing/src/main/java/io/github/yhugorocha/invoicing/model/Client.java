package io.github.yhugorocha.invoicing.model;

public record Client(Long id,
                     String name,
                     String cpf,
                     String street,
                     String number,
                     String neighborhood,
                     String email,
                     String phone) {
}
