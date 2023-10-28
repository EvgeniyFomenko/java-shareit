package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.AlreadyEmailExistException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceImplTest {
    @Mock
    UserRepository userRepository;
    @InjectMocks
    UserServiceImpl userService;

    @Test
    public void getAllTestTwoUserDtoTest() {
        User userOne = User.builder().id(1).build();
        User userTwo = User.builder().id(2).build();
        List<User> userList = new ArrayList<>();
        Collections.addAll(userList, userOne, userTwo);
        List<UserDto> userDto = userList.stream().map(UserMapper::mapperToDto).collect(Collectors.toList());
        when(userRepository.findAll()).thenReturn(userList);
        List<UserDto> userDtos = userService.getAll();
        assertEquals(2, userDtos.size());
    }

    @Test
    public void getUserByIdTest() {
        User userOne = User.builder().id(1).build();
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(userOne));

        UserDto userDto = userService.get(1);
        assertNotNull(userDto);
    }
    @Test
    public void getUserByIdExceptionTest(){
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class,() -> userService.get(1));
    }

    @Test
    public void createUserTest(){
        User userOne = User.builder().id(1).name("user").email("email@mail.com").build();
        when(userRepository.save(Mockito.any())).thenReturn(userOne);
        assertNotNull(userService.create(UserMapper.mapperToDto(userOne)));
    }

    @Test
    public void updateFailTest(){
        User userOne = User.builder().id(1).name("user").email("email@mail.com").build();
        User userTwo = User.builder().id(2).name("user").email("email@mail.com").build();
        when(userRepository.findUserByEmail(Mockito.any())).thenReturn(userTwo);
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(userOne));
        when(userRepository.save(Mockito.any())).thenReturn(userOne);
        assertThrows(AlreadyEmailExistException.class,() -> userService.update(UserMapper.mapperToDto(userOne)));

    }

    @Test
    public void updateTest(){
        UserMapper userMapper = new UserMapper();
        Assertions.assertNotNull(userMapper);
        User userOne = User.builder().id(1).build();
        when(userRepository.findUserByEmail(Mockito.any())).thenReturn(userOne);
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(userOne));
        when(userRepository.save(Mockito.any())).thenReturn(userOne);
        UserDto userDto = userService.update(UserMapper.mapperToDto(userOne));
        assertNotNull(userDto);
        when(userRepository.findUserByEmail(Mockito.any())).thenReturn(null);
        userDto = userService.update(UserMapper.mapperToDto(userOne));
        assertNotNull(userDto);
    }

    @Test
    public void deleteTest(){
        userService.delete(1L);
        verify(userRepository, atMostOnce()).deleteById(1L);

    }

}