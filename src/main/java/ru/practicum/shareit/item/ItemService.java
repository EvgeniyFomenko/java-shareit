package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    List<Item> getAll(long userId);

    Item get(long id);

    Item create(Item item, long userId);

    Item update(Item item, long userId);

    void delete(long id);

    List<ItemDto> search(String text);
}
