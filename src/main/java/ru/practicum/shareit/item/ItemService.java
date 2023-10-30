package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentAnswerDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    List<ItemDto> getAll(long userId, Integer from, Integer size);

    ItemDto get(long itemId, long userId);

    ItemDto create(ItemDto itemDto, long userId);

    ItemDto update(ItemDto item, long userId);

    void delete(long id);

    List<ItemDto> search(String text, Integer from, Integer size);

    CommentAnswerDto postCommentByItemId(CommentDto comment);
}
