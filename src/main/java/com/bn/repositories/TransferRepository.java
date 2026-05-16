package com.bn.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bn.entities.Transfer;

public interface TransferRepository extends JpaRepository<Transfer, UUID> {
}