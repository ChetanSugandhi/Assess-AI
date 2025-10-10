package com.AssessAI.AssessAI.repository;

import com.AssessAI.AssessAI.models.AppRole;
import com.AssessAI.AssessAI.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByRoleName(AppRole appRole);
}
