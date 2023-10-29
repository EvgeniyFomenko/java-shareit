package ru.practicum.shareit.item;

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
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.dto.CommentAnswerDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
class ItemControllerTest {
    @Mock
    ItemServiceImpl itemService;
    @InjectMocks
    ItemController itemController;
    ObjectMapper mapper = new ObjectMapper();
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(itemController)
                .setControllerAdvice(new ControllerException())
                .build();
    }

    @Test
    public void saveItem() throws Exception {
        ItemDto itemDto = ItemDto.builder().id(1).available(true).name("item").description("description").ownerId(1).build();
        Mockito.when(itemService.create(Mockito.any(), Mockito.anyLong())).thenReturn(itemDto);
        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getAllItems() throws Exception {
        List<ItemDto> itemList = new ArrayList<>();
        Mockito.when(itemService.getAll(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(itemList);
        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getAllBadPagination() throws Exception {
        Mockito.when(itemService.getAll(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt())).thenThrow(BadArgumentsPaginationException.class);
        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getItemById() throws Exception {
        ItemDto itemDto = ItemDto.builder().id(1).available(true).name("item").description("description").ownerId(1).build();
        Mockito.when(itemService.get(Mockito.anyLong(), Mockito.anyLong())).thenReturn(itemDto);
        mvc.perform(get("/items/{id}", itemDto.getId())
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getItemByIdUserNotFound() throws Exception {
        ItemDto itemDto = ItemDto.builder().id(1).available(true).name("item").description("description").ownerId(1).build();
        Mockito.when(itemService.get(Mockito.anyLong(), Mockito.anyLong())).thenThrow(UserNotFoundException.class);
        mvc.perform(get("/items/{id}", itemDto.getId())
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getItemByIdItemNotFound() throws Exception {
        ItemDto itemDto = ItemDto.builder().id(1).available(true).name("item").description("description").ownerId(1).build();
        Mockito.when(itemService.get(Mockito.anyLong(), Mockito.anyLong())).thenThrow(ItemNotFoundException.class);
        mvc.perform(get("/items/{id}", itemDto.getId())
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteItemById() throws Exception {
        ItemDto itemDto = ItemDto.builder().id(1).available(true).name("item").description("description").ownerId(1).build();
        mvc.perform(delete("/items/{id}", itemDto.getId())
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void updateItem() throws Exception {
        ItemDto itemDto = ItemDto.builder().id(1).available(true).name("item").description("description").ownerId(1).build();
        Mockito.when(itemService.update(Mockito.any(), Mockito.anyLong())).thenReturn(itemDto);
        mvc.perform(patch("/items/{id}", itemDto.getId())
                        .content(mapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void updateItemException() throws Exception {
        ItemDto itemDto = ItemDto.builder().id(1).available(true).name("item").description("description").ownerId(1).build();
        Mockito.when(itemService.update(Mockito.any(), Mockito.anyLong())).thenThrow(AccessDenideException.class);
        mvc.perform(patch("/items/{id}", itemDto.getId())
                        .content(mapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void searchItem() throws Exception {
        ItemDto itemDto = ItemDto.builder().id(1).available(true).name("item").description("description").ownerId(1).build();
        Mockito.when(itemService.search(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(Collections.emptyList());
        mvc.perform(get("/items/search?text='text'")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void postComment() throws Exception {
        CommentDto commentDto = CommentDto.builder().text("text").itemId(1).id(1).build();
        CommentAnswerDto commentAnswerDto = CommentAnswerDto.builder().id(1).text("text").authorName("name").build();
        Mockito.when(itemService.postCommentByItemId(Mockito.any())).thenReturn(commentAnswerDto);
        mvc.perform(post("/items/{id}/comment", 1L)
                        .content(mapper.writeValueAsString(commentDto))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void postCommentOwnerHasNotItem() throws Exception {
        CommentDto commentDto = CommentDto.builder().text("text").itemId(1).id(1).build();
        CommentAnswerDto commentAnswerDto = CommentAnswerDto.builder().id(1).text("text").authorName("name").build();
        Mockito.when(itemService.postCommentByItemId(Mockito.any())).thenThrow(OwnerHasNotItemException.class);
        mvc.perform(post("/items/{id}/comment", 1L)
                        .content(mapper.writeValueAsString(commentDto))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void postCommentChangeException() throws Exception {
        CommentDto commentDto = CommentDto.builder().text("text").itemId(1).id(1).build();
        CommentAnswerDto commentAnswerDto = CommentAnswerDto.builder().id(1).text("text").authorName("name").build();
        Mockito.when(itemService.postCommentByItemId(Mockito.any())).thenThrow(ChangeDeprecated.class);
        mvc.perform(post("/items/{id}/comment", 1L)
                        .content(mapper.writeValueAsString(commentDto))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


}