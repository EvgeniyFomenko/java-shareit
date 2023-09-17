package ru.practicum.shareit.booking;

import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import java.time.Instant;

/**
 * TODO Sprint add-bookings.
 */

@Data
public class Booking {
    private int id;
    private Item item;
    private Instant start;
    private Instant end;
    private StatusBooking status;
    private User broker;
}
