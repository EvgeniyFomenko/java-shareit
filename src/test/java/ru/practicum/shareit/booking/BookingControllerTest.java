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
import ru.practicum.shareit.exception.ControllerException;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {
    @InjectMocks
    BookingController bookingController;
    @Mock
    BookingServiceImpl bookingService;
    private MockMvc mvc;
    ObjectMapper mapper = new ObjectMapper();

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
    public void getOwner() throws Exception {
        mvc.perform(get("/bookings/owner?state=all&from=1&size=1")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


}