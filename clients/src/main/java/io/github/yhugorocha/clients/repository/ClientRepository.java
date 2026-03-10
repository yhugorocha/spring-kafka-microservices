package io.github.yhugorocha.clients.repository;

import io.github.yhugorocha.clients.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
