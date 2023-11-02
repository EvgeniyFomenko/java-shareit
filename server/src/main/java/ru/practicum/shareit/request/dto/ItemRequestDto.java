package ru.practicum.shareit.request.dto;

import lombok.*;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestDto {
    private long id;
    private long requester;
    @NonNull
    private String description;
    private LocalDateTime created;
}
