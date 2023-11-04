package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingForItemDto;

import java.util.Objects;

public class BookingForItemMapper {
    public static BookingForItemDto toDto(Booking booking) {
        if (Objects.isNull(booking)) {
            return null;
        }
        return BookingForItemDto.builder().itemId(booking.getItem().getId())
                .bookerId(booking.getIdBooker())
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatus())
                .build();

    }
}
