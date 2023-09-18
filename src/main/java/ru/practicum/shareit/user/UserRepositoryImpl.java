package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final Map<Long, User> users;
    private long id;

    @Override
    public List<User> getAll() {
        return List.copyOf(users.values());
    }

    @Override
    public User get(long id) {
        return users.get(id);
    }

    @Override
    public User create(User user) {
        id++;
        user.setId(id);
        users.put(id,user);

        return users.get(id);
    }

    @Override
    public User update(User user) {
        users.put(user.getId(),user);
        return users.get(user.getId());
    }

    @Override
    public void delete(long id) {
        users.remove(id);
    }
}
