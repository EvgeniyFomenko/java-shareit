package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInputStatusDto;
import ru.practicum.shareit.user.User;

public class BookingMapper {
    public static BookingDto toBookingDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .booker(User.builder().id(booking.getIdBooker()).build())
                .item(booking.getItem())
                .status(booking.getStatus())
                .build();
    }

    public static BookingInputStatusDto toBookingInputStatusDto(long id, String status, long userId) {
        return BookingInputStatusDto.builder().bookingId(id)
                .userId(userId)
                .state(status).build();
    }
}
