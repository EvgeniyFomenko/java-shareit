package ru.practicum.shareit.request.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String description;
    @OneToOne
    @JoinColumn(name = "requestor_id")
    private User requester;
    private LocalDateTime created;
    @OneToMany
    @JoinColumn(name = "request_id")
    private List<Item> items = new ArrayList<>();
}
