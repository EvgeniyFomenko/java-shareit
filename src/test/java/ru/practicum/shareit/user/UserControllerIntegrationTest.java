package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDto;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserControllerIntegrationTest {
    @Autowired
    UserServiceImpl userService;

    @Test
    public void saveTest() {
        UserDto userDto = UserDto.builder().name("User").email("email@test.ru").build();
        UserDto userDtoTest = userService.create(userDto);
        assertNotNull(userDtoTest);
    }

}