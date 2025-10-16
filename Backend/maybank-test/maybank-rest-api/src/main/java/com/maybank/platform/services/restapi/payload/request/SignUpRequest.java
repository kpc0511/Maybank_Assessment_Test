package com.maybank.platform.services.restapi.payload.request;

import com.maybank.platform.services.restapi.annotation.PasswordMatches;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
@PasswordMatches
public class SignUpRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    private Set<String> role;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

    @NotBlank
    @Size(min = 6, max = 40)
    private String matchingPassword;

    @NotBlank
    @Size(max = 120)
    private String phone;

    @Size(max = 120)
    private String displayName;

    @Size(max = 300)
    private String remark;
}
