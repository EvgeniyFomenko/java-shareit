package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    List<Item> getAll();

    Item get(long id);

    Item create(Item item);

    Item update(Item item);

    void delete(long id);
}
