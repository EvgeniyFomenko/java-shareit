package ru.practicum.shareit.request.entity;

import lombok.Builder;
import ru.practicum.shareit.user.User;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;

@Builder
public class ItemRequestSave {
    private long id;
    private String description;
    @OneToOne
    @JoinColumn(name = "id")
    @Column(name = "requester_id")
    private User requester;
    private LocalDateTime created;
    @Column(name = "item_id")
    private long itemId;
}
