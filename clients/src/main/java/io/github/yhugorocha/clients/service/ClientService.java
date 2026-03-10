package io.github.yhugorocha.clients.service;

import io.github.yhugorocha.clients.dto.ClientRequest;
import io.github.yhugorocha.clients.dto.ClientResponse;
import io.github.yhugorocha.clients.entity.Client;
import io.github.yhugorocha.clients.repository.ClientRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientResponse create(ClientRequest request) {
        Client client = Client.builder()
                .name(request.name())
                .cpf(request.cpf())
                .street(request.street())
                .number(request.number())
                .neighborhood(request.neighborhood())
                .email(request.email())
                .phone(request.phone())
                .build();

        return ClientResponse.fromEntity(clientRepository.save(client));
    }

    public ClientResponse findById(Long id) {
        return clientRepository.findById(id)
                .map(ClientResponse::fromEntity)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Client not found with id " + id
                ));
    }

    public List<ClientResponse> findAll() {
        return clientRepository.findAll()
                .stream()
                .map(ClientResponse::fromEntity)
                .toList();
    }
}
