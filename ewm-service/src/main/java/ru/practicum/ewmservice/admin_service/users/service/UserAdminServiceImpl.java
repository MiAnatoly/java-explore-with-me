package ru.practicum.ewmservice.admin_service.users.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmservice.dao.users.UsersRepository;
import ru.practicum.ewmservice.dto.users.NewUserRequest;
import ru.practicum.ewmservice.dto.users.UserDto;
import ru.practicum.ewmservice.mapper.UsersMapper;
import ru.practicum.ewmservice.model.user.User;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserAdminServiceImpl implements UserAdminService {
    private final UsersRepository repository;

    @Override
    public List<UserDto> findAll(List<Long> ids, Integer page, Integer size) {
        List<User> users = repository.findByIdIn(ids, PageRequest.of(page, size)).getContent();
        log.info("get users: {} serviceAdmin", users.size());
        return UsersMapper.toUsersDto(users);
    }

    @Transactional
    @Override
    public UserDto add(NewUserRequest newUserRequest) {
        User user = UsersMapper.toUser(newUserRequest);
        user = repository.save(user);
        UserDto userDto = UsersMapper.toUserDto(user);
        log.info("add user: {} serviceAdmin", userDto.getName());
        return userDto;
    }

    @Transactional
    @Override
    public void remove(Long userId) {
        repository.deleteById(userId);
        log.info("remove user: {} serviceAdmin", userId);
    }
}
