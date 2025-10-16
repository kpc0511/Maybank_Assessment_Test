package com.maybank.platform.services.restapi.payload.response;

import lombok.Data;

import java.util.List;

@Data
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private Long userId;
    private String username;
    private String email;
    private List<String> roles;
    private String lng;

    public JwtResponse(String accessToken, Long id, String username, String email, String lng, List<String> roles) {
        this.token = accessToken;
        this.id = id;
        this.userId = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
        this.lng = lng;
    }
}
