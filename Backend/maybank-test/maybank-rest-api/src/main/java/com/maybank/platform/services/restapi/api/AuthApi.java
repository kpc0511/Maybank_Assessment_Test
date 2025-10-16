package com.maybank.platform.services.restapi.api;

import com.maybank.platform.services.restapi.payload.request.LoginRequest;
import com.maybank.platform.services.restapi.payload.request.SignUpRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/auth")
public interface AuthApi {

    @PostMapping("/signIn")
    ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest);

    @PostMapping("/signUp")
    ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest);

    @PostMapping("/logout")
    ResponseEntity<?> logoutUser(HttpServletRequest request);
}
