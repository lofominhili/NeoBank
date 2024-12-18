package com.lofominhili.deal.repository;

import com.lofominhili.deal.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID> {

}
