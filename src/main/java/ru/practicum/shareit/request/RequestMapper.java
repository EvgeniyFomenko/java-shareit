package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.entity.ItemRequest;
import ru.practicum.shareit.user.User;

public class RequestMapper {
    public static ItemRequest fromDto(ItemRequestDto requestDto) {
        return ItemRequest.builder()
                .description(requestDto.getDescription())
                .requester(User.builder().id(requestDto.getRequester()).build())
                .created(requestDto.getCreated())
                .build();
    }

    public static ItemRequestDto toDto(ItemRequest itemRequest) {
        return ItemRequestDto.builder()
                .requester(itemRequest.getId())
                .id(itemRequest.getId())
                .created(itemRequest.getCreated())
                .description(itemRequest.getDescription())
                .build();
    }

    public static RequestDto toRequestDto(ItemRequest itemRequest) {
        return RequestDto.builder()
                .requester(itemRequest.getId())
                .id(itemRequest.getId())
                .created(itemRequest.getCreated())
                .description(itemRequest.getDescription())
                .items(itemRequest.getItems())
                .build();
    }

}
