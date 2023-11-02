package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.exception.BadArgumentsPaginationException;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> getBookings(long userId, BookingState state, Integer from, Integer size) {
        if (Objects.isNull(from) || Objects.isNull(size)) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
        }

        if (from < 0 || size <= 0) {
            throw new BadArgumentsPaginationException("такой страницы не существует");
        }
        Map<String, Object> parameters = Map.of(
                "state", state.name(),
                "from", from,
                "size", size
        );
        return get("?state={state}&from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> getBookingsForOwner(long userId, BookingState state, Integer from, Integer size) {
        if (Objects.isNull(from) || Objects.isNull(size)) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
        }

        if (from < 0 || size <= 0) {
            throw new BadArgumentsPaginationException("такой страницы не существует");
        }

        Map<String, Object> parameters = Map.of(
                "state", state.name(),
                "from", from,
                "size", size
        );
        return get("/owner?state={state}&from={from}&size={size}", userId, parameters);
    }


    public ResponseEntity<Object> bookItem(long userId, BookItemRequestDto requestDto) {
        return post("", userId, requestDto);
    }

    public ResponseEntity<Object> getBooking(long userId, Long bookingId) {
        return get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> responseToBookingRequest(long bookingId, String approved, long userId) {
        Map<String, Object> parameters = Map.of(
                "approved", approved
        );
        return (patch("/" + bookingId + "?approved={approved}", userId, parameters, null));
    }
}
