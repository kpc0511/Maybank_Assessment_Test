package com.maybank.platform.services.restapi.mapper;

import com.maybank.platform.services.restapi.model.Role;
import com.maybank.platform.services.restapi.model.User;
import com.maybank.platform.services.restapi.payload.response.RoleResponseDto;
import com.maybank.platform.services.restapi.payload.response.UserResponseDto;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserMapper {
    public UserResponseDto toDto(User user) {
        if (user == null) return null;
        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setDisplayName(user.getDisplayName());
        dto.setEnabled(user.isEnabled());
        dto.setRemark(user.getRemark());
        dto.setLanguage(user.getLanguage());
        dto.setLastLoginDate(user.getLastLoginDate());
        dto.setRoles(
                user.getRoles().stream()
                        .map(this::toRoleDto)
                        .collect(Collectors.toSet())
        );
        return dto;
    }

    private RoleResponseDto toRoleDto(Role role) {
        RoleResponseDto dto = new RoleResponseDto();
        dto.setId(role.getId());
        dto.setName(role.getName().name());
        return dto;
    }
}
