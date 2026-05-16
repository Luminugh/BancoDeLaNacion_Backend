package com.bn.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bn.entities.Account;

public interface AccountRepository extends JpaRepository<Account, UUID> {
}