package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingItemDto;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto addResponseToBooking(@RequestBody @Valid BookingItemDto bookingItemDto, @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.create(bookingItemDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto responseToBookingRequest(@PathVariable long bookingId, @RequestParam String approved, @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.updateBookingStatusById(BookingMapper.toBookingInputStatusDto(bookingId, approved, userId));
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@PathVariable long bookingId, @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.get(BookingMapper.toBookingInputStatusDto(bookingId, null, userId));
    }

    @GetMapping
    public List<BookingDto> getAllBookingForUser(@RequestParam(defaultValue = "ALL") String state, @RequestHeader("X-Sharer-User-Id") long userId, @RequestParam(defaultValue = "0", required = false) Integer from, @RequestParam(defaultValue = "10", required = false) Integer size) {
        return bookingService.getAllBookingForUser(BookingMapper.toBookingInputStatusDto(0, state, userId), from, size);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllBookingForOwnerItem(@RequestParam(defaultValue = "ALL") String state, @RequestHeader("X-Sharer-User-Id") long userId, @RequestParam(defaultValue = "0", required = false) Integer from, @RequestParam(defaultValue = "10", required = false) Integer size) {
        return bookingService.getAllBookingForOwnerItem(BookingMapper.toBookingInputStatusDto(0, state, userId), from, size);
    }


}
