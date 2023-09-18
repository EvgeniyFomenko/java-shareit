package ru.practicum.shareit.request;

import lombok.Data;
import ru.practicum.shareit.user.User;

import java.time.Instant;

/**
 * TODO Sprint add-item-requests.
 */

@Data
public class ItemRequest {
    private String item;
    private String description;
    private User requester;
    private Instant created;
}
