package io.github.yhugorocha.invoicing.model;

public record Client(String name,
                     String cpf,
                     String street,
                     String number,
                     String neighborhood,
                     String email,
                     String phone) {
}
