package com.stanislavgrujic.documentimporter.repository;

import com.stanislavgrujic.documentimporter.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  User findByEmail(String email);
}
