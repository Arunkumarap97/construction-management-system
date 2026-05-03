package com.ark.construction.repository;

import com.ark.construction.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Long> {
    List<Client> findByActiveTrue();

    Long countByActiveTrue();

    @Query("SELECT c FROM Client c")
    List<Client> findAllClients();
}