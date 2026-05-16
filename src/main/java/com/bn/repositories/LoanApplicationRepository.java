package com.bn.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bn.entities.LoanApplication;

public interface LoanApplicationRepository extends JpaRepository<LoanApplication, UUID> {
}