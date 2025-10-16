package com.maybank.platform.services.restapi.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
public class ProfileRequest {
    @NotNull(message = "Please enter id")
    private Long id;

    @Size(max = 120)
    private String displayName;

    @Size(max = 120)
    private String phone;

    @Size(max = 300)
    private String remark;
    private boolean enabled;
    private Set<String> role;
    private String updateBy;
    private String language;
}
