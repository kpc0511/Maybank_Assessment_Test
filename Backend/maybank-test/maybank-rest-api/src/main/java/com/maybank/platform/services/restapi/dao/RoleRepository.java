package com.maybank.platform.services.restapi.dao;

import com.maybank.platform.services.restapi.model.Role;
import com.maybank.platform.services.util.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
    List<Role> findAll();
}
