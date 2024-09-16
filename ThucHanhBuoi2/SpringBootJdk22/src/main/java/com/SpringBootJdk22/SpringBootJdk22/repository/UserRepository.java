package com.SpringBootJdk22.SpringBootJdk22.repository;

import com.SpringBootJdk22.SpringBootJdk22.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}

