package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

public class ItemDtoMapper {
    public static ItemDto mapToDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .ownerId(item.getIdOwner())
                .available(item.getAvailable())
                .lastBooking(item.getLastBooking())
                .nextBooking(item.getNextBooking())
                .comments(item.getComments())
                .requestId(item.getRequestId())
                .build();
    }

    public static Item mapToItem(ItemDto item) {
        return Item.builder()
                .name(item.getName())
                .description(item.getDescription())
                .idOwner(item.getOwnerId())
                .available(item.getAvailable())
                .id(item.getId())
                .requestId(item.getRequestId())
                .build();
    }

}
