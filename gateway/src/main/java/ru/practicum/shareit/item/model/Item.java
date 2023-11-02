package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingForItemDto;
import ru.practicum.shareit.item.dto.CommentAnswerDto;

import javax.persistence.*;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */

@Data
@Builder
@Entity
@Table(name = "item")
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    @Column(name = "id_owner")
    private long idOwner;
    private Boolean available;
    @OneToOne
    @Transient
    private Booking booking;
    @Transient
    private BookingForItemDto lastBooking;
    @Transient
    private BookingForItemDto nextBooking;
    @Transient
    private List<CommentAnswerDto> comments;
    @Column(name = "request_id")
    private Long requestId;
}
