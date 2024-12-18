package com.lofominhili.deal.repository;

import com.lofominhili.deal.entity.Statement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StatementRepository extends JpaRepository<Statement, UUID> {

}
