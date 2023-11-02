package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInputStatusDto;
import ru.practicum.shareit.booking.dto.BookingItemDto;

import java.util.List;

public interface BookingService {
    BookingDto get(BookingInputStatusDto bookingInputStatusDto);

    BookingDto create(BookingItemDto bookingItemDto, long userId);

    Booking update(Booking booking);

    void delete(int id);

    BookingDto updateBookingStatusById(BookingInputStatusDto bookingInputStatusDto);

    List<BookingDto> getAllBookingForUser(BookingInputStatusDto bookingInputStatusDto,Integer from, Integer size);

    List<BookingDto> getAllBookingForOwnerItem(BookingInputStatusDto bookingInputStatusDto, Integer from, Integer size);

}
