package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.booking.StatusBooking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Builder
@Getter
@Setter
@ToString
public class BookingDto {
    private long id;
    private Item item;
    private LocalDateTime start;
    private LocalDateTime end;
    private StatusBooking status;
    private User booker;
}
