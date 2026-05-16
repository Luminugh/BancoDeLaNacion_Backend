package com.bn.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bn.entities.Loan;

public interface LoanRepository extends JpaRepository<Loan, UUID> {
}