package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.AlreadyEmailExistException;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<UserDto> getAll() {
        return userRepository.findAll().stream().map(UserMapper::mapperToDto).collect(Collectors.toList());
    }

    @Override
    public UserDto get(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("пользователь с таким id не найден"));

        return UserMapper.mapperToDto(user);
    }

    @Override
    public UserDto create(@Valid UserDto user) {
        User user1 = userRepository.save(UserMapper.mapperFromDto(user));
        return UserMapper.mapperToDto(user1);
    }

    private void validation(User user) {
        if (Objects.nonNull(userRepository.findUserByEmail(user.getEmail()))) {
            throw new AlreadyEmailExistException("Пользователь с таким email уже существует");
        }

    }

    @Override
    public UserDto update(UserDto userDto) {
        User user = UserMapper.mapperFromDto(userDto);
        validateEmptyField(user);
        User findUser = userRepository.findUserByEmail(user.getEmail());

        if (Objects.nonNull(findUser) && !Objects.equals(user.getId(), findUser.getId())) {
            throw new AlreadyEmailExistException("Пользователь с таким email уже существует");
        }
        return UserMapper.mapperToDto(userRepository.save(user));
    }

    private void validateEmptyField(User user) {
        User orig = UserMapper.mapperFromDto(get(user.getId()));

        if (Objects.isNull(user.getName())) {
            user.setName(orig.getName());
        }

        if (Objects.isNull(user.getEmail())) {
            user.setEmail(orig.getEmail());
        }
    }

    @Override
    public void delete(long id) {
        userRepository.deleteById(id);
    }
}
