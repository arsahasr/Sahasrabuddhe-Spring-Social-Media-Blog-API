package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository <Account, Integer> { // The Jpa repository offers pre-built methods for common database operations
}