package com.ark.construction.service;

import com.ark.construction.entity.Client;
import com.ark.construction.entity.Payment;
import com.ark.construction.entity.Project;
import com.ark.construction.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClientService {

    private final ClientRepository repository;

    public ClientService(ClientRepository repository) {
        this.repository = repository;
    }

    public List<Client> getAllActiveClients() {
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

    public List<Client> getAllClients() {
        return repository.findAllClients();
    }

    // ===============================
    // CLIENT DETAIL PAGE LOGIC
    // ===============================

    public List<Project> getProjectsOfClient(Long clientId) {
        Client client = getClientById(clientId);
        return client.getProjects();
    }

    public int totalProjects(Long clientId) {
        Client client = getClientById(clientId);
        return client.getProjects() != null ? client.getProjects().size() : 0;
    }

    public Double totalProjectCost(Long clientId) {
        Client client = getClientById(clientId);

        double total = 0;

        if (client.getProjects() != null) {
            for (Project p : client.getProjects()) {
                if (p.getTotalCost() != null) {
                    total += p.getTotalCost();
                }
            }
        }

        return total;
    }

    public Double totalReceived(Long clientId) {
        Client client = getClientById(clientId);

        double total = 0;

        if (client.getProjects() != null) {
            for (Project project : client.getProjects()) {
                if (project.getPayments() != null) {
                    for (Payment payment : project.getPayments()) {
                        if (payment.getAmount() != null) {
                            total += payment.getAmount();
                        }
                    }
                }
            }
        }

        return total;
    }

    public Double pendingAmount(Long clientId) {
        return totalProjectCost(clientId) - totalReceived(clientId);
    }

    public List<Payment> getAllPaymentsOfClient(Long clientId) {
        Client client = getClientById(clientId);

        List<Payment> allPayments = new ArrayList<>();

        if (client.getProjects() != null) {
            for (Project project : client.getProjects()) {
                if (project.getPayments() != null) {
                    allPayments.addAll(project.getPayments());
                }
            }
        }

        return allPayments;
    }
}