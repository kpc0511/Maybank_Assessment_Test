package com.maybank.platform.services.restapi.mapper.decorator;

import com.maybank.platform.services.restapi.mapper.UserMapper;
import com.maybank.platform.services.restapi.model.User;
import com.maybank.platform.services.restapi.payload.response.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserMapperDecorator {
    private final UserMapper delegate;

    public UserResponseDto toDtoWithLogic(User user) {
        if (user == null) return null;

        UserResponseDto dto = delegate.toDto(user);

        // Example: localization handling
        if (dto.getLanguage() == null) {
            dto.setLanguage("en-US");
        }

        return dto;
    }

    public List<UserResponseDto> toDtoListWithLogic(List<User> users) {
        if (users == null || users.isEmpty()) return List.of();
        return users.stream().map(this::toDtoWithLogic).toList();
    }
}
