package com.bn.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bn.entities.Payment;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
}