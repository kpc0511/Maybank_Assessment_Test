package com.maybank.platform.services.restapi.api.authentication;

import com.alibaba.fastjson2.JSONObject;
import com.maybank.platform.services.restapi.api.AuthApi;
import com.maybank.platform.services.restapi.api.BaseController;
import com.maybank.platform.services.restapi.common.LocaleMsgUtils;
import com.maybank.platform.services.restapi.config.AESProperties;
import com.maybank.platform.services.restapi.config.security.TokenProvider;
import com.maybank.platform.services.restapi.exceptions.UserAlreadyExistAuthenticationException;
import com.maybank.platform.services.restapi.model.User;
import com.maybank.platform.services.restapi.payload.request.LoginRequest;
import com.maybank.platform.services.restapi.payload.request.SignUpRequest;
import com.maybank.platform.services.restapi.payload.response.ApiResponse;
import com.maybank.platform.services.restapi.payload.response.JwtResponse;
import com.maybank.platform.services.restapi.payload.response.UserResponseDto;
import com.maybank.platform.services.restapi.services.UserDetailsImpl;
import com.maybank.platform.services.restapi.services.UserService;
import com.maybank.platform.services.util.AES256Utils;
import com.maybank.platform.services.util.QueryStringParser;
import com.maybank.platform.services.util.RedisDbKeyUtils;
import com.maybank.platform.services.util.RedisUtil;
import com.maybank.platform.services.util.constants.MessagesConstant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController extends BaseController implements AuthApi {
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final UserService userService;
    private final AESProperties aesProperties;
    private final RedisUtil redisUtil;

    @Override
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        String username;
        String password;
        if(!ObjectUtils.isEmpty(loginRequest.getData())) {
            String data = loginRequest.getData();
            String decryptedStr = AES256Utils.decrypt(data, aesProperties.getSecretKey());
            if(!ObjectUtils.isEmpty(decryptedStr)) {
                JSONObject jsonObject = QueryStringParser.parse(decryptedStr);
                username = jsonObject.getString("username");
                password = jsonObject.getString("password");
            } else {
                return ResponseEntity
                        .badRequest()
                        .body(new ApiResponse(false, LocaleMsgUtils.getMsg(MessagesConstant.USER_NOT_FOUND)));
            }
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(new ApiResponse(false, LocaleMsgUtils.getMsg(MessagesConstant.USER_NOT_FOUND)));
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = tokenProvider.createToken(authentication);

            long expiryDuration = tokenProvider.getExpiryDuration(jwt);
            String key = RedisDbKeyUtils.getRedisCacheRestApiTokenKey(jwt);
            redisUtil.put(key, jwt, expiryDuration);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority).collect(Collectors.toList());
            if(!ObjectUtils.isEmpty(userDetails)) {
                Optional<User> optionalUser = userService.findByUsername(userDetails.getUsername());
                if(optionalUser.isPresent()) {
                    User user = optionalUser.get();
                    user.setLastLoginDate(LocalDateTime.now());
                    userService.save(user);
                }
            }
            return ResponseEntity.ok(new JwtResponse(jwt,
                    userDetails.getId(),
                    userDetails.getUsername(),
                    userDetails.getEmail(),
                    userDetails.getLanguage(),
                    roles));
        } catch (BadCredentialsException e) {
            // Handle bad credentials exception
            return ResponseEntity
                    .badRequest()
                    .body(new ApiResponse(false, LocaleMsgUtils.getMsg(MessagesConstant.USER_NOT_FOUND)));
        } catch (Exception e) {
            // Handle any other exceptions
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, LocaleMsgUtils.getMsg(MessagesConstant.REQUEST_PROCESSING_ERROR)));
        }

    }

    @Override
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        try {
            UserResponseDto userResponseDto = userService.registerUser(signUpRequest);
            return ResponseEntity.ok().body(userResponseDto);
        } catch (UserAlreadyExistAuthenticationException e) {
            log.error("Exception:", e);
            return new ResponseEntity<>(
                    new ApiResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<?> logoutUser(HttpServletRequest request) {
        String jwt = tokenProvider.resolveToken(request);
        if (jwt != null) {
            String key = RedisDbKeyUtils.getRedisCacheRestApiTokenKey(jwt);
            redisUtil.delete(key);
            // Remove token from Redis
            SecurityContextHolder.clearContext(); // Clear the security context

            return ResponseEntity.ok(new ApiResponse(true, "Logged out successfully."));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, "No token provided."));
    }
}
