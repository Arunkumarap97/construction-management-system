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
        return repository.findAll();
    }

    public Client saveClient(Client client) {
        return repository.save(client);
    }

    public void deleteClient(Long id) {
        repository.deleteById(id);
    }

    public Client getClientById(Long id) {
        return repository.findById(id).orElse(null);
    }
}