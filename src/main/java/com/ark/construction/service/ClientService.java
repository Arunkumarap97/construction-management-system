package com.ark.construction.service;

import com.ark.construction.entity.Client;
import com.ark.construction.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    private final ClientRepository repository;

    public ClientService(ClientRepository repository) {
        this.repository = repository;
    }

    public List<Client> getAllClients() {
        return repository.findByActiveTrue();
    }

    public Client saveClient(Client client) {
        return repository.save(client);
    }

    public void deleteClient(Long id) {
        Client client = repository.findById(id).orElse(null);
        if (client != null) {
            client.setActive(false);   // 👈 soft delete
            repository.save(client);
        }
    }

    public Client getClientById(Long id) {
        return repository.findById(id).orElse(null);
    }
}