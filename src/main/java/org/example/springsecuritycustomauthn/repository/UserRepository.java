package org.example.springsecuritycustomauthn.repository;

import org.example.springsecuritycustomauthn.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsername(String username);
}