package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getAll() {
        return userService.getAll().stream().map(UserMapper::mapperToDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UserDto getById(@PathVariable long id) {
        return UserMapper.mapperToDto(userService.get(id));
    }

    @PostMapping
    public UserDto create(@RequestBody @Valid UserDto user) {
        return UserMapper.mapperToDto(userService.create(UserMapper.mapperFromDto(user)));
    }

    @PatchMapping("/{id}")
    public UserDto update(@RequestBody UserDto user,@PathVariable long id) {
        user.setId(id);
        return UserMapper.mapperToDto(userService.update(UserMapper.mapperFromDto(user)));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        userService.delete(id);
    }

}
