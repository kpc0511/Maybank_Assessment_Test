package com.maybank.platform.services.restapi.services;

import com.maybank.platform.services.restapi.exceptions.UserAlreadyExistAuthenticationException;
import com.maybank.platform.services.restapi.model.User;
import com.maybank.platform.services.restapi.payload.request.SignUpRequest;
import com.maybank.platform.services.restapi.payload.request.UserListingRequest;
import com.maybank.platform.services.restapi.payload.response.UserResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserService {
    UserResponseDto registerUser(SignUpRequest signUpRequest) throws UserAlreadyExistAuthenticationException;
    Optional<User> findByUsername(String username);
    Optional<User> findUserById(Long id);
    void save(User user);
    Page<User> findAll(Pageable pageable);
    Page<User> findByDisplayName(String displayName, Pageable pageable);
    String getCurrentUser();
    Page<User> queryUsers(UserListingRequest request, Pageable pageable);
}
