package ru.practicum.shareit.user;

import java.util.List;

public interface UserRepository {
    List<User> getAll();

    User get(long id);

    User create(User user);

    User update(User user);

    void delete(long id);
}
