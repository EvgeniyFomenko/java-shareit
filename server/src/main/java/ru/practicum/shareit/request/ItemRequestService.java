package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.entity.ItemRequest;

import java.util.List;

public interface ItemRequestService {
    RequestDto get(long userId, int id);

    ItemRequestDto create(ItemRequestDto itemRequest);

    List<ItemRequest> getAll(long userId, Integer form, Integer size);

    List<ItemRequest> getAll(long userId);
}
