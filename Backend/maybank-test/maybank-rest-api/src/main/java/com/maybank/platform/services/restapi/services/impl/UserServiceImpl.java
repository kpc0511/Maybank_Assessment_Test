package com.maybank.platform.services.restapi.services.impl;

import com.maybank.platform.services.restapi.common.LocaleMsgUtils;
import com.maybank.platform.services.restapi.dao.UserRepository;
import com.maybank.platform.services.restapi.dao.specs.UserSpecification;
import com.maybank.platform.services.restapi.exceptions.UserAlreadyExistAuthenticationException;
import com.maybank.platform.services.restapi.mapper.decorator.UserMapperDecorator;
import com.maybank.platform.services.restapi.model.Role;
import com.maybank.platform.services.restapi.model.User;
import com.maybank.platform.services.restapi.payload.request.SignUpRequest;
import com.maybank.platform.services.restapi.payload.request.UserListingRequest;
import com.maybank.platform.services.restapi.payload.response.UserResponseDto;
import com.maybank.platform.services.restapi.services.RoleService;
import com.maybank.platform.services.restapi.services.UserService;
import com.maybank.platform.services.util.SnowflakeIdGenerator;
import com.maybank.platform.services.util.constants.MessagesConstant;
import com.maybank.platform.services.util.enums.ERole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final UserMapperDecorator userMapperDecorator;

    @Override
    public String getCurrentUser() {
        Authentication authenticationToken = SecurityContextHolder.getContext().getAuthentication();
        return authenticationToken.getName();
    }

    @Override
    @Transactional
    public UserResponseDto registerUser(SignUpRequest signUpRequest) throws UserAlreadyExistAuthenticationException {
        if (userRepository.existsByUsername(signUpRequest.getUsername()).equals(Boolean.TRUE)) {
            log.error("Username already exist: {}", signUpRequest.getUsername());
            throw new UserAlreadyExistAuthenticationException(LocaleMsgUtils.getMsg(MessagesConstant.USERNAME_EXISTED));
        }
        if (userRepository.existsByEmail(signUpRequest.getEmail()).equals(Boolean.TRUE)) {
            log.error("Email already exist: {}", signUpRequest.getEmail());
            throw new UserAlreadyExistAuthenticationException(LocaleMsgUtils.getMsg(MessagesConstant.EMAIL_EXISTED));
        }
        List<Role> roleList = roleService.getAllRoles();
        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();
        if (CollectionUtils.isEmpty(strRoles)) {
            Role userRole = roleList.stream().filter(r->r.getName().equals(ERole.ROLE_USER)).findAny()
                    .orElseThrow(() -> new RuntimeException(LocaleMsgUtils.getMsg(MessagesConstant.ROLE_NOT_FOUND)));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleList.stream().filter(r->r.getName().equals(ERole.ROLE_ADMIN)).findAny()
                                .orElseThrow(() -> new RuntimeException(LocaleMsgUtils.getMsg(MessagesConstant.ROLE_NOT_FOUND)));
                        roles.add(adminRole);
                        break;
                    case "mod":
                        Role modRole = roleList.stream().filter(r->r.getName().equals(ERole.ROLE_MODERATOR)).findAny()
                                .orElseThrow(() -> new RuntimeException(LocaleMsgUtils.getMsg(MessagesConstant.ROLE_NOT_FOUND)));
                        roles.add(modRole);
                        break;
                    default:
                        Role userRole = roleList.stream().filter(r->r.getName().equals(ERole.ROLE_USER)).findAny()
                                .orElseThrow(() -> new RuntimeException(LocaleMsgUtils.getMsg(MessagesConstant.ROLE_NOT_FOUND)));
                        roles.add(userRole);
                }
            });
        }
        User user = User.builder().username(signUpRequest.getUsername()).email(signUpRequest.getEmail())
                .password(passwordEncoder.encode(signUpRequest.getPassword())).roles(roles)
                .displayName(signUpRequest.getDisplayName()).enabled(true).phone(signUpRequest.getPhone())
                .remark(signUpRequest.getRemark()).id(SnowflakeIdGenerator.generateId())
                .build();
        user.setCreateDate(new Date());
        user.setCreateBy(signUpRequest.getUsername());
        user = userRepository.save(user);
        return userMapperDecorator.toDtoWithLogic(user);
    }

    @Override
    @Transactional
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> findByDisplayName(String displayName, Pageable pageable) {
        return userRepository.findByDisplayName(displayName, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> queryUsers(UserListingRequest request, Pageable pageable) {
        return userRepository.findAll(UserSpecification.filter(request), pageable);
    }
}
