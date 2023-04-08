package ru.practicum.ewmservice.admin_service.users.service;

import ru.practicum.ewmservice.dto.users.NewUserRequest;
import ru.practicum.ewmservice.dto.users.UserDto;

import java.util.List;

public interface UserAdminService {

    List<UserDto> findAll(List<Long> ids, Integer page, Integer size);

    UserDto add(NewUserRequest user);

    void remove(Long userId);
}
