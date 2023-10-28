package ru.practicum.shareit.booking.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class BookingItemDto {
    @NonNull
    private long itemId;
    @NonNull
    private LocalDateTime start;
    @NonNull
    private LocalDateTime end;
}
