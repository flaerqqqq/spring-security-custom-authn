package org.example.springsecuritycustomauthn.repository;

import org.example.springsecuritycustomauthn.model.entity.Role;
import org.example.springsecuritycustomauthn.model.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleName name);
}