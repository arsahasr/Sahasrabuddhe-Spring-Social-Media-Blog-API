package com.example.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Message;

@Repository
@Transactional
public interface MessageRepository extends JpaRepository <Message, Integer> { // The Jpa repository offers pre-built methods for common database operations
}
