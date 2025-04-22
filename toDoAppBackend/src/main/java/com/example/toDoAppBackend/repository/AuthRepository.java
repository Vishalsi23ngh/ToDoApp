package com.example.toDoAppBackend.repository;

import com.example.toDoAppBackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRepository extends JpaRepository<User, Long> {
}
