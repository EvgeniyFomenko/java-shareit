package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.item.model.Item;

public class BookingItemMapper {
    public static Booking fromDto(BookingItemDto bookingItemDto) {
        return Booking.builder()
                .item(Item.builder().id(bookingItemDto.getItemId()).build())
                .status(StatusBooking.WAITING)
                .start(bookingItemDto.getStart())
                .end(bookingItemDto.getEnd())
                .build();
    }

    public static BookingItemDto toDto(Booking bookingItemDto) {
        return BookingItemDto.builder()
                .itemId(bookingItemDto.getItem().getId())
                .start(bookingItemDto.getStart())
                .end(bookingItemDto.getEnd())
                .build();
    }

}
