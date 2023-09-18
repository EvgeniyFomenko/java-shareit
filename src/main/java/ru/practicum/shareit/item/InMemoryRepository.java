package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Repository
public class InMemoryRepository implements ItemRepository {
    private final Map<Long, Item> items;
    private long ids;

    @Override
    public List<Item> getAll() {
        return List.copyOf(items.values());
    }

    @Override
    public Item get(long id) {
        return items.get(id);
    }

    @Override
    public Item create(Item item) {
        ids++;
        item.setId(ids);
        items.put(item.getId(), item);

        return items.get(ids);
    }

    @Override
    public Item update(Item item) {
        items.put(item.getId(), item);

        return items.get(item.getId());
    }

    @Override
    public void delete(long id) {
        items.remove(id);
    }
}
