package com.fintech.financialservice.persistance.repository;

import com.fintech.financialservice.persistance.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
