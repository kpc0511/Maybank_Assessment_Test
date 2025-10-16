package com.maybank.platform.services.restapi.api;

import com.maybank.platform.services.restapi.common.LocaleMsgUtils;
import com.maybank.platform.services.restapi.config.AESProperties;
import com.maybank.platform.services.restapi.config.security.TokenProvider;
import com.maybank.platform.services.restapi.mapper.decorator.UserMapperDecorator;
import com.maybank.platform.services.restapi.model.Role;
import com.maybank.platform.services.restapi.model.User;
import com.maybank.platform.services.restapi.payload.request.ProfileRequest;
import com.maybank.platform.services.restapi.payload.request.UserListingRequest;
import com.maybank.platform.services.restapi.payload.response.ApiResponse;
import com.maybank.platform.services.restapi.payload.response.UserResponseDto;
import com.maybank.platform.services.restapi.services.RoleService;
import com.maybank.platform.services.restapi.services.UserService;
import com.maybank.platform.services.util.RedisUtil;
import com.maybank.platform.services.util.constants.MessagesConstant;
import com.maybank.platform.services.util.enums.ERole;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController extends BaseController implements UserApi {
    private final UserService userService;
    private final RoleService roleService;
    private final UserMapperDecorator userMapperDecorator;

    @Override
    public ResponseEntity<Map<String, Object>> getUserListing(@Valid @RequestBody UserListingRequest userListingRequest){
        Map<String, Object> response = new HashMap<>();
        try {
            List<Sort.Order> orders = new ArrayList<>();
            if (userListingRequest.getSort()[0].contains(",")) {
                // will sort more than 2 fields
                // sortOrder="field, direction"
                for (String sortOrder : userListingRequest.getSort()) {
                    String[] sorts = sortOrder.split(",");
                    orders.add(new Sort.Order(getSortDirection(sorts[1]), sorts[0]));
                }
            } else {
                // sort=[field, direction]
                orders.add(new Sort.Order(getSortDirection(userListingRequest.getSort()[1]), userListingRequest.getSort()[0]));
            }
            Pageable pagingSort = PageRequest.of(userListingRequest.getPage() - 1, userListingRequest.getSize(), Sort.by(orders));
            Page<User> userPage = userService.queryUsers(userListingRequest, pagingSort);

            List<UserResponseDto> userResponseDtos = userPage.getContent().stream()
                    .map(userMapperDecorator::toDtoWithLogic)
                    .toList();
            response.put("success", true);
            response.put("message", LocaleMsgUtils.getMsg(MessagesConstant.TESTING));
            response.put("users", userResponseDtos);
            response.put("currentPage", userPage.getNumber() + 1);
            response.put("totalItems", userPage.getTotalElements());
            response.put("totalPages", userPage.getTotalPages());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> saveProfile(@Valid @RequestBody ProfileRequest profileRequest) {
        Optional<User> optionalUser = userService.findUserById(profileRequest.getId());
        if (optionalUser.isPresent()) {
            Set<String> strRoles = profileRequest.getRole();
            Set<Role> roles = new HashSet<>();
            List<Role> roleList = roleService.getAllRoles();
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
            User newUser = optionalUser.get().toBuilder().roles(roles).displayName(profileRequest.getDisplayName())
                    .enabled(profileRequest.isEnabled()).remark(profileRequest.getRemark()).phone(profileRequest.getPhone()).build();
            newUser.setUpdateDate(new Date());
            newUser.setUpdateBy(profileRequest.getUpdateBy());
            userService.save(newUser);
            return ResponseEntity.ok(new ApiResponse(true, LocaleMsgUtils.getMsg(MessagesConstant.USER_UPDATE_SUCCESS)));
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(new ApiResponse(false, LocaleMsgUtils.getMsg(MessagesConstant.USER_NOT_FOUND)));
        }
    }

    @Override
    public ResponseEntity<?> changeLng(@Valid @RequestBody ProfileRequest profileRequest) {
        Optional<User> optionalUser = userService.findUserById(profileRequest.getId());
        if (optionalUser.isPresent()) {
            User newUser = optionalUser.get().toBuilder().language(profileRequest.getLanguage()).build();
            newUser.setUpdateDate(new Date());
            newUser.setUpdateBy(profileRequest.getUpdateBy());
            userService.save(newUser);
            return ResponseEntity.ok(new ApiResponse(true, LocaleMsgUtils.getMsg(MessagesConstant.USER_UPDATE_SUCCESS)));
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(new ApiResponse(false, LocaleMsgUtils.getMsg(MessagesConstant.USER_NOT_FOUND)));
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> getUserNotice(){
        Map<String, Object> response = new HashMap<>();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private Sort.Direction getSortDirection(String direction) {
        if (direction.equals("asc")) {
            return Sort.Direction.ASC;
        } else if (direction.equals("desc")) {
            return Sort.Direction.DESC;
        }
        return Sort.Direction.ASC;
    }
}
