package ru.practicum.shareit.request;

public interface ItemRequestService {
    ItemRequest get(int id);

    ItemRequest create(ItemRequest itemRequest);

    ItemRequest update(ItemRequest itemRequest);

    void delete(int id);
}
