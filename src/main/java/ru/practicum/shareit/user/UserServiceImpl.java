package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.exception.AlreadyEmailExistException;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public List<User> getAll() {
        return userRepository.getAll();
    }

    @Override
    public User get(long id) {
        User user = userRepository.get(id);
        if (Objects.isNull(user)) {
            throw new UserNotFoundException("пользователь с таким id не найден");
        }

        return user;
    }

    @Override
    public User create(User user) {
        validation(user);

        return userRepository.create(user);
    }

    private void validation(User user) {
        if (userRepository.getAll().stream().anyMatch(user1 -> Objects.equals(user1.getEmail(), user.getEmail()))) {
            throw new AlreadyEmailExistException("Пользователь с таким email уже существует");
        }

    }

    @Override
    public User update(User user) {
        validateEmptyField(user);
        if (userRepository.getAll().stream().anyMatch(user1 -> Objects.equals(user1.getEmail(), user.getEmail()) && !Objects.equals(user.getId(), user1.getId()))) {
            throw new AlreadyEmailExistException("Пользователь с таким email уже существует");
        }
        return userRepository.update(user);
    }

    private void validateEmptyField(User user) {
        User orig = userRepository.get(user.getId());

        if (Objects.isNull(user.getName())) {
            user.setName(orig.getName());
        }

        if (Objects.isNull(user.getEmail())) {
            user.setEmail(orig.getEmail());
        }
    }

    @Override
    public void delete(long id) {
        userRepository.delete(id);
        itemRepository.getAll().stream().filter(e -> Objects.equals(e.getIdOwner(), id)).peek(e -> itemRepository.delete(e.getId())).close();
    }
}
