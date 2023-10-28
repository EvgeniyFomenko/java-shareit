package ru.practicum.shareit.request;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.exception.BadArgumentsPaginationException;
import ru.practicum.shareit.exception.BookingNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.entity.ItemRequest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@SpringBootTest
class ItemRequestServiceImplTest {

    @Mock
    ItemRequestRepository itemRequestRepository;
    @Mock
    UserRepository userRepository;
    @InjectMocks
    ItemRequestServiceImpl itemRequestService;

    @Test
    void getTest() {
        User user = User.builder().id(1).name("Name").email("email").build();
        Item itemOne = Item.builder()
                .name("item1")
                .id(1L)
                .available(true)
                .idOwner(1L)
                .description("description1")
                .build();
        ItemRequest itemRequest = ItemRequest.builder().items(List.of(itemOne)).requester(user).description("description").build();
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        when(itemRequestRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(itemRequest));
        RequestDto requestDto = itemRequestService.get(1L, 1);
        Assertions.assertNotNull(requestDto);
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(UserNotFoundException.class, () -> itemRequestService.get(1L, 1));
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        when(itemRequestRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(BookingNotFoundException.class, () -> itemRequestService.get(1L, 1));


    }

    @Test
    void createTest() {
        User user = User.builder().id(1).name("Name").email("email").build();
        Item itemOne = Item.builder()
                .name("item1")
                .id(1L)
                .available(true)
                .idOwner(1L)
                .description("description1")
                .build();
        ItemRequest itemRequest = ItemRequest.builder().id(1L).items(List.of(itemOne)).requester(user).description("description").build();
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        when(itemRequestRepository.save(Mockito.any())).thenReturn(itemRequest);
        ItemRequestDto itemRequestDto = RequestMapper.toDto(itemRequest);
        ItemRequestDto itemRequestDto1 = itemRequestService.create(itemRequestDto);
        Assertions.assertNotNull(itemRequestDto1);
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(UserNotFoundException.class,()-> itemRequestService.create(itemRequestDto));
    }

    @Test
    void getAllTestEmptyList() {
        List<ItemRequest> itemRequests = itemRequestService.getAll(1, null, null);
        Assertions.assertEquals(0, itemRequests.size());
        itemRequests = itemRequestService.getAll(1, 1, null);
        Assertions.assertEquals(0, itemRequests.size());
        itemRequests = itemRequestService.getAll(1, null, 1);
        Assertions.assertEquals(0, itemRequests.size());
    }

    @Test
    void getAllZeroFromSize() {
        Assertions.assertThrows(BadArgumentsPaginationException.class, () -> itemRequestService.getAll(1, 0, 0));
        Assertions.assertThrows(BadArgumentsPaginationException.class, () -> itemRequestService.getAll(1, -1, 0));
        Assertions.assertThrows(BadArgumentsPaginationException.class, () -> itemRequestService.getAll(1, -1, -1));
    }

    @Test
    void getAllSuccess() {
        User user = User.builder().id(1).name("Name").email("email").build();
        Item itemOne = Item.builder()
                .name("item1")
                .id(1L)
                .available(true)
                .idOwner(1L)
                .description("description1")
                .build();
        ItemRequest itemRequest = ItemRequest.builder().id(1L).items(List.of(itemOne)).requester(user).description("description").build();
        ItemRequest itemRequestOne = ItemRequest.builder().id(2L).items(List.of(itemOne)).requester(user).description("description").build();
        List<ItemRequest> itemRequests = new ArrayList<>();
        Collections.addAll(itemRequests, itemRequestOne, itemRequest);
        Page<ItemRequest> itemRequestsPage = new PageImpl(itemRequests, PageRequest.of(1, 5, Sort.Direction.ASC, "id"), itemRequests.size());
        when(itemRequestRepository.findAllByRequesterIdNot(Mockito.anyLong(), Mockito.any())).thenReturn(itemRequestsPage);
        List<ItemRequest> itemRequestsFinal = itemRequestService.getAll(1, 1, 1);
        Assertions.assertEquals(2, itemRequestsFinal.size());
        itemRequestsFinal = itemRequestService.getAll(1, 0, 1);
        Assertions.assertEquals(2, itemRequestsFinal.size());
    }

    @Test
    void getAllByIdException() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(UserNotFoundException.class, () -> itemRequestService.getAll(1));
    }

    @Test
    void getAllById() {
        Item itemOne = Item.builder()
                .name("item1")
                .id(1L)
                .available(true)
                .idOwner(1L)
                .description("description1")
                .build();
        User user = User.builder().id(1).build();
        ItemRequest itemRequest = ItemRequest.builder().id(1L).items(List.of(itemOne)).requester(user).description("description").build();
        ItemRequest itemRequestOne = ItemRequest.builder().id(2L).items(List.of(itemOne)).requester(user).description("description").build();
        List<ItemRequest> itemRequests = new ArrayList<>();
        Collections.addAll(itemRequests, itemRequestOne, itemRequest);
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        when(itemRequestRepository.findAllByRequesterId(1)).thenReturn(itemRequests);
        List<ItemRequest> itemRequestsList = itemRequestService.getAll(1);
        Assertions.assertEquals(2, itemRequestsList.size());
        RequestMapper requestMapper = new RequestMapper();
        Assertions.assertNotNull(requestMapper);
    }

}