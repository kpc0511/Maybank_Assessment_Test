package com.maybank.platform.services.restapi.services;

import com.maybank.platform.services.restapi.model.Role;
import com.maybank.platform.services.util.enums.ERole;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    Optional<Role> findByName(ERole name);
    List<Role> getAllRoles();
}
