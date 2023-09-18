package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.StatusBooking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.Instant;

/**
 * TODO Sprint add-bookings.
 */
public class BookingDto {
    private int id;
    private Item item;
    private Instant start;
    private Instant end;
    private StatusBooking status;
    private User broker;
}
