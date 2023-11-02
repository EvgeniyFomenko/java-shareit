package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> getAll() {
        log.info("get all user");
        return userClient.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable long id) {
        log.info("get by id user id = {}", id);
        return userClient.get(id);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid UserDto user) {
        log.info("create user id = {}, user name = {}, user email = {}", user.getId(), user.getName(), user.getEmail());
        return userClient.create(user);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@RequestBody UserDto user, @PathVariable long id) {
        user.setId(id);
        log.info("update user id = {}, user name = {}, user email = {}", user.getId(), user.getName(), user.getEmail());
        return userClient.update(user);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        log.info("delete user id = {}", id);
        userClient.delete(id);
    }
}
