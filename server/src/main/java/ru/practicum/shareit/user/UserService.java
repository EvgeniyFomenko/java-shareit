package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface  UserService {
    List<UserDto> getAll();

    UserDto get(long id);

    UserDto create(UserDto user);

    UserDto update(UserDto user);

    void delete(long id);
}
