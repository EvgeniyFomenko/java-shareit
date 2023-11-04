package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.booking.StatusBooking;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@ToString
public class BookingForItemDto {
    private long id;
    private Long itemId;
    private LocalDateTime start;
    private LocalDateTime end;
    private StatusBooking status;
    private Long bookerId;
}
