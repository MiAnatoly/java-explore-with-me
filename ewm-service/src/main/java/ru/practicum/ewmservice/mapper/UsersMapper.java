package ru.practicum.ewmservice.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.ewmservice.dto.users.NewUserRequest;
import ru.practicum.ewmservice.dto.users.UserDto;
import ru.practicum.ewmservice.dto.users.UserShortDto;
import ru.practicum.ewmservice.model.user.User;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UsersMapper {

    public static User toUser(NewUserRequest userRequest) {
        return new User(
                null,
                userRequest.getEmail(),
                userRequest.getName()
        );
    }

    public static UserShortDto toUserShortDto(User user) {
        return new UserShortDto(
                user.getId(),
                user.getName()
        );
    }

    public static UserDto toUserDto(User user) {
        return new UserDto(
                user.getEmail(),
                user.getId(),
                user.getName()
        );
    }

    public static List<UserDto> toUsersDto(List<User> users) {
        return users.stream().map(UsersMapper::toUserDto).collect(Collectors.toList());
    }
}
