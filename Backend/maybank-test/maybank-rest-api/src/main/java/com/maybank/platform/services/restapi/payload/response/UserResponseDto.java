package com.maybank.platform.services.restapi.payload.response;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    private Long id;
    private String username;
    private String email;
    private String phone;
    private String displayName;
    private boolean enabled;
    private String remark;
    private String language;
    private LocalDateTime lastLoginDate;
    private Set<RoleResponseDto> roles;
}
