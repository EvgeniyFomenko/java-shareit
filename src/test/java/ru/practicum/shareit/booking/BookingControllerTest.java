package ru.practicum.shareit.booking;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.exception.*;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {
    @InjectMocks
    BookingController bookingController;
    @Mock
    BookingServiceImpl bookingService;
    ObjectMapper mapper = new ObjectMapper();
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(bookingController)
                .setControllerAdvice(ControllerException.class)
                .build();
        mapper.registerModule(new JavaTimeModule());

    }

    @Test
    public void postBooking() throws Exception {
        BookingDto bookingDto = BookingDto.builder().id(1).status(StatusBooking.WAITING).build();
        BookingItemDto bookingItemDto = BookingItemDto.builder()
                .itemId(1)
                .end(LocalDateTime.now().plusDays(2))
                .start(LocalDateTime.now()).build();
        Mockito.when(bookingService.create(Mockito.any(), Mockito.anyLong())).thenReturn(bookingDto);
        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingItemDto))
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        Mockito.verify(bookingService, Mockito.atMostOnce()).create(Mockito.any(), Mockito.anyLong());
    }

    @Test
    public void updateBooking() throws Exception {
        BookingDto bookingDto = BookingDto.builder().id(1).status(StatusBooking.WAITING).build();
        BookingItemDto bookingItemDto = BookingItemDto.builder()
                .itemId(1)
                .end(LocalDateTime.now().plusDays(2))
                .start(LocalDateTime.now()).build();
        Mockito.when(bookingService.updateBookingStatusById(Mockito.any())).thenReturn(bookingDto);
        mvc.perform(patch("/bookings/1?approved=true")
                        .content(mapper.writeValueAsString(bookingItemDto))
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        Mockito.verify(bookingService, Mockito.atMostOnce()).updateBookingStatusById(Mockito.any());
    }

    @Test
    public void getById() throws Exception {
        BookingDto bookingDto = BookingDto.builder().id(1).status(StatusBooking.WAITING).build();
        Mockito.when(bookingService.get(Mockito.any())).thenReturn(bookingDto);
        mvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        Mockito.verify(bookingService, Mockito.atMostOnce()).get(Mockito.any());
    }

    @Test
    public void getAllBook() throws Exception {
        mvc.perform(get("/bookings/1?state=all&from=1&size=1")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getAllBookingForUser() throws Exception {
        mvc.perform(get("/bookings?state=all&from=1&size=1")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        Mockito.verify(bookingService, Mockito.times(1)).getAllBookingForUser(Mockito.any(), Mockito.anyInt(), Mockito.anyInt());
    }

    @Test
    public void getOwner() throws Exception {
        mvc.perform(get("/bookings/owner?state=all&from=1&size=1")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getForUserException() throws Exception {
        Mockito.when(bookingService.getAllBookingForUser(Mockito.any(), Mockito.anyInt(), Mockito.anyInt())).thenThrow(UnknownStatus.class);
        mvc.perform(get("/bookings?state=all&from=1&size=1")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());
    }

    @Test
    public void postBookingTimeException() throws Exception {
        Mockito.when(bookingService.create(Mockito.any(), Mockito.anyLong())).thenThrow(BookingTimeException.class);
        BookingItemDto bookingItemDto = BookingItemDto.builder().itemId(1).start(LocalDateTime.now()).end(LocalDateTime.now().plusDays(2)).build();
        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingItemDto))
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void postBookingItemUnavailableFoundException() throws Exception {
        Mockito.when(bookingService.create(Mockito.any(), Mockito.anyLong())).thenThrow(ItemUnavailableException.class);
        BookingItemDto bookingItemDto = BookingItemDto.builder().itemId(1).start(LocalDateTime.now()).end(LocalDateTime.now().plusDays(2)).build();
        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingItemDto))
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void postBookingChangeDeprecatedException() throws Exception {
        Mockito.when(bookingService.updateBookingStatusById(Mockito.any())).thenThrow(ChangeDeprecated.class);
        BookingItemDto bookingItemDto = BookingItemDto.builder().itemId(1).start(LocalDateTime.now()).end(LocalDateTime.now().plusDays(2)).build();
        mvc.perform(patch("/bookings/1?approved=true")
                        .content(mapper.writeValueAsString(bookingItemDto))
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void postBookingNotFoundStateException() throws Exception {
        Mockito.when(bookingService.updateBookingStatusById(Mockito.any())).thenThrow(NotFoundStateException.class);
        BookingItemDto bookingItemDto = BookingItemDto.builder().itemId(1).start(LocalDateTime.now()).end(LocalDateTime.now().plusDays(2)).build();
        mvc.perform(patch("/bookings/1?approved=true")
                        .content(mapper.writeValueAsString(bookingItemDto))
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void postBookingNotFoundException() throws Exception {
        Mockito.when(bookingService.updateBookingStatusById(Mockito.any())).thenThrow(BookingNotFoundException.class);
        BookingItemDto bookingItemDto = BookingItemDto.builder().itemId(1).start(LocalDateTime.now()).end(LocalDateTime.now().plusDays(2)).build();
        mvc.perform(patch("/bookings/1?approved=true")
                        .content(mapper.writeValueAsString(bookingItemDto))
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }


}