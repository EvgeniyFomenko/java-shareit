package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import ru.practicum.shareit.exception.ControllerException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.entity.ItemRequest;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ItemRequestControllerTest {
    @InjectMocks
    ItemRequestController itemRequestController;
    @Mock
    ItemRequestServiceImpl itemRequestService;

    MockMvc mvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(itemRequestController)
                .setControllerAdvice(new ControllerException())
                .build();
    }

    @Test
    public void postRequest() throws Exception{
        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .requester(1)
                .description("descr")
                .build();
        Mockito.when(itemRequestService.create(Mockito.any())).thenReturn(itemRequestDto);
        mvc.perform(post("/requests")
                .content(objectMapper.writeValueAsString(itemRequestDto))
                .header("X-Sharer-User-Id",1)
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getRequests() throws Exception{
        List<ItemRequest> itemRequestList = Collections.emptyList();
        Mockito.when(itemRequestService.getAll(Mockito.anyLong())).thenReturn(Collections.emptyList());
        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id",1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getRequestsById() throws Exception{
        RequestDto itemRequestDto = RequestDto.builder()
                .requester(1)
                .description("descr")
                .build();
        Mockito.when(itemRequestService.get(Mockito.anyLong(),Mockito.anyInt())).thenReturn(itemRequestDto);
        mvc.perform(get("/requests/{id}",1L)
                        .header("X-Sharer-User-Id",1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getRequestsPagination() throws Exception{
        List<ItemRequest> itemRequestList = Collections.emptyList();
        Mockito.when(itemRequestService.getAll(Mockito.anyLong(),Mockito.anyInt(),Mockito.anyInt())).thenReturn(Collections.emptyList());
        mvc.perform(get("/requests/all?from=1&size=0")
                        .header("X-Sharer-User-Id",1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}