package com.maybank.platform.services.restapi.api;

import com.maybank.platform.services.restapi.payload.request.ProfileRequest;
import com.maybank.platform.services.restapi.payload.request.UserListingRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@RequestMapping("/api/user")
public interface UserApi {

    @PostMapping("/saveProfile")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    ResponseEntity<?> saveProfile(@Valid @RequestBody ProfileRequest profileRequest);

    @PostMapping("/getUserListing")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    ResponseEntity<Map<String, Object>> getUserListing(@Valid @RequestBody UserListingRequest userListingRequest);

    @GetMapping("/notice")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('MODERATOR')")
    ResponseEntity<Map<String, Object>> getUserNotice();

    @PostMapping("/changeLng")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('MODERATOR')")
    ResponseEntity<?> changeLng(@Valid @RequestBody ProfileRequest profileRequest);
}
