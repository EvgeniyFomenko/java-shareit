package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

public class UserMapper {
    public static UserDto mapperToDto(User user) {
        return UserDto.builder().id(user.getId()).name(user.getName()).email(user.getEmail()).build();
    }

    public static User mapperFromDto(UserDto userDto) {
        return User.builder().id(userDto.getId()).name(userDto.getName()).email(userDto.getEmail()).build();
    }
}
