package ru.practicum.shareit.item;

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
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.dto.CommentAnswerDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repositories.CommentRepository;
import ru.practicum.shareit.item.repositories.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class ItemServiceImplTest {
    @Mock
    ItemRepository itemRepository;
    @Mock
    BookingRepository bookingRepository;
    @Mock
    CommentRepository commentRepository;
    @Mock
    UserRepository userRepository;
    @InjectMocks
    ItemServiceImpl itemService;

    @Test
    public void getAllWithFromSizeNullTest() {
        List<ItemDto> itemDtoList = itemService.getAll(1, null, 1);
        assertEquals(0, itemDtoList.size());
        itemDtoList = itemService.getAll(1, 1, null);
        assertEquals(0, itemDtoList.size());
        itemDtoList = itemService.getAll(1, null, null);
        assertEquals(0, itemDtoList.size());
    }

    @Test
    public void getAllWithFromSizeZeroBadArgumentsPaginationExceptionTest() {
        assertThrows(BadArgumentsPaginationException.class, () -> itemService.getAll(1, 0, 0));
        assertThrows(BadArgumentsPaginationException.class, () -> itemService.getAll(1, -1, 0));
        assertThrows(BadArgumentsPaginationException.class, () -> itemService.getAll(1, -1, 1));
    }

    @Test
    public void getAllSuccessTest() {
        Item itemOne = Item.builder()
                .name("item1")
                .id(1L)
                .available(true)
                .idOwner(1L)
                .description("description1")
                .build();
        Item itemTwo = Item.builder()
                .name("item2")
                .id(1L)
                .available(true)
                .idOwner(1L)
                .description("description2")
                .build();
        List<Item> itemList = new ArrayList<>();
        Collections.addAll(itemList, itemOne, itemTwo);
        Page<Item> page = new PageImpl(itemList, PageRequest.of(0, 2, Sort.Direction.ASC, "id"), itemList.size());
        when(commentRepository.findAllByItemId(Mockito.anyLong())).thenReturn(Optional.of(Collections.emptyList()));
        when(bookingRepository.findFirstByItemIdOwnerAndStartIsBeforeAndStatusOrderByStartDesc(Mockito.anyLong(), Mockito.any(), Mockito.any())).thenReturn(Booking.builder().id(1L).item(Item.builder().id(1L).build()).build());
        when(bookingRepository.findFirstByItemIdOwnerAndStartIsAfterAndStatusOrderByStartAsc(Mockito.anyLong(), Mockito.any(), Mockito.any())).thenReturn(Booking.builder().id(1L).item(Item.builder().id(1L).build()).build());
        when(itemRepository.findAllByIdOwner(Mockito.anyLong(), Mockito.any())).thenReturn(page);
        List<ItemDto> getItemList = itemService.getAll(1, 1, 1);
        Assertions.assertEquals(2, getItemList.size());
        when(bookingRepository.findFirstByItemIdOwnerAndStartIsBeforeAndStatusOrderByStartDesc(Mockito.anyLong(), Mockito.any(), Mockito.any())).thenReturn(null);
        when(bookingRepository.findFirstByItemIdOwnerAndStartIsAfterAndStatusOrderByStartAsc(Mockito.anyLong(), Mockito.any(), Mockito.any())).thenReturn(null);
        getItemList = itemService.getAll(1, 0, 1);
        Assertions.assertEquals(2, getItemList.size());

        when(bookingRepository.findFirstByItemIdOwnerAndStartIsBeforeAndStatusOrderByStartDesc(Mockito.anyLong(), Mockito.any(), Mockito.any())).thenReturn(Booking.builder().id(1L).item(Item.builder().id(99L).build()).build());
        when(bookingRepository.findFirstByItemIdOwnerAndStartIsAfterAndStatusOrderByStartAsc(Mockito.anyLong(), Mockito.any(), Mockito.any())).thenReturn(Booking.builder().id(1L).item(Item.builder().id(100L).build()).build());
        getItemList = itemService.getAll(1, 0, 1);
        Assertions.assertEquals(2, getItemList.size());
        ItemDtoMapper itemDtoMapper = new ItemDtoMapper();
        assertNotNull(itemDtoMapper);
    }

    @Test
    public void getBookingByIdTest() {
        User user = User.builder().id(1l).name("User").email("email").build();
        Item itemOne = Item.builder()
                .name("item1")
                .id(1L)
                .available(true)
                .idOwner(1L)
                .description("description1")
                .build();

        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(itemOne));
        when(commentRepository.findAllByItemId(Mockito.anyLong())).thenReturn(Optional.of(Collections.emptyList()));
        when(bookingRepository.findFirstByItemIdOwnerAndStartIsBeforeAndStatusOrderByStartDesc(Mockito.anyLong(), Mockito.any(), Mockito.any())).thenReturn(Booking.builder().id(1L).item(Item.builder().id(1L).build()).build());
        when(bookingRepository.findFirstByItemIdOwnerAndStartIsAfterAndStatusOrderByStartAsc(Mockito.anyLong(), Mockito.any(), Mockito.any())).thenReturn(Booking.builder().id(1L).item(Item.builder().id(1L).build()).build());
        ItemDto itemDto = itemService.get(1, 1);
        Assertions.assertNotNull(itemDto);
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(UserNotFoundException.class, () -> itemService.get(1, 1));
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(ItemNotFoundException.class, () -> itemService.get(1, 1));
    }

    @Test
    public void createBookingTest() {
        User user = User.builder().id(1l).name("User").email("email").build();
        Item itemOne = Item.builder()
                .name("item1")
                .id(1L)
                .available(true)
                .idOwner(1L)
                .description("description1")
                .build();
        ItemDto itemDto = ItemDtoMapper.mapToDto(itemOne);
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.save(Mockito.any())).thenReturn(itemOne);
        ItemDto itemDto1 = itemService.create(itemDto, 1);
        assertNotNull(itemDto1);
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> itemService.create(itemDto, 1));
    }

    @Test
    public void updateItemSuccessTest() {
        User user = User.builder().id(1l).name("User").email("email").build();
        Item itemOne = Item.builder()
                .name("item1")
                .id(1L)
                .available(true)
                .idOwner(1L)
                .description("description1")
                .requestId(1l)
                .comments(Collections.emptyList())
                .booking(Booking.builder().idBooker(1L).id(1).build())
                .build();
        ItemDto itemDto = ItemDtoMapper.mapToDto(itemOne);
        when(commentRepository.findAllByItemId(Mockito.anyLong())).thenReturn(Optional.of(Collections.emptyList()));
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(itemOne));
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.save(Mockito.any())).thenReturn(itemOne);
        ItemDto itemDto1 = itemService.update(itemDto, 1);
        assertNotNull(itemDto1);
    }

    @Test
    public void updateItemNullItemNameDescriptionAvailableTest() {
        User user = User.builder().id(1l).name("User").email("email").build();
        Item itemOne = Item.builder()
                .id(1L)
                .idOwner(1L)
                .requestId(1l)
                .comments(Collections.emptyList())
                .booking(Booking.builder().idBooker(1L).id(1).build())
                .build();
        ItemDto itemDto = ItemDtoMapper.mapToDto(itemOne);
        when(commentRepository.findAllByItemId(Mockito.anyLong())).thenReturn(Optional.of(Collections.emptyList()));
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(itemOne));
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.save(Mockito.any())).thenReturn(itemOne);
        ItemDto itemDto1 = itemService.update(itemDto, 1);
        assertNotNull(itemDto1);
    }

    @Test
    public void updateItemNotSuccessTest() {
        Item itemOne = Item.builder()
                .name("item1")
                .id(1L)
                .available(true)
                .idOwner(1L)
                .description("description1")
                .build();
        ItemDto itemDto = ItemDtoMapper.mapToDto(itemOne);
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(itemOne));
        Assertions.assertThrows(AccessDenideException.class, () -> itemService.update(itemDto, 2L));
    }

    @Test
    public void deleteTest() {
        itemService.delete(1);
        verify(itemRepository, atMostOnce()).deleteById(Mockito.anyLong());
    }

    @Test
    public void searchTextIsBlankTest() {
        assertEquals(0, itemService.search("", 1, 0).size());
    }

    @Test
    public void searchTextFromSizeNull() {
        assertEquals(0, itemService.search("text", null, null).size());
        assertEquals(0, itemService.search("text", 1, null).size());
        assertEquals(0, itemService.search("text", null, 1).size());
    }

    @Test
    public void searchTextFormSizeOne() {
        assertThrows(BadArgumentsPaginationException.class, () -> itemService.search("text", 0, 0).size());
        assertThrows(BadArgumentsPaginationException.class, () -> itemService.search("text", -1, 0).size());
        assertThrows(BadArgumentsPaginationException.class, () -> itemService.search("text", -1, -1).size());
    }

    @Test
    public void searchTextSucces() {
        Item itemOne = Item.builder()
                .name("item1")
                .id(1L)
                .available(true)
                .idOwner(1L)
                .description("description1")
                .build();
        Item itemTwo = Item.builder()
                .name("item2")
                .id(1L)
                .available(true)
                .idOwner(1L)
                .description("description2")
                .build();
        List<Item> itemList = new ArrayList<>();
        Collections.addAll(itemList, itemOne, itemTwo);
        Page<Item> page = new PageImpl(itemList, PageRequest.of(0, 2, Sort.Direction.ASC, "id"), itemList.size());

        when(itemRepository.findAllByDescriptionContainingIgnoreCaseAndAvailableTrue(any(), any())).thenReturn(page);
        List<ItemDto> itemDtoList = itemService.search("Mock", 1, 1);
        assertEquals(2, itemDtoList.size());
        itemDtoList = itemService.search("Mock", 0, 1);
        assertEquals(2, itemDtoList.size());
    }

    @Test
    public void postCommentThrowExceptionOwnerHasNotItem() {
        CommentDto commentDto = CommentDto.builder().itemId(1).text("Text").id(1).build();
        when(bookingRepository.findFirstByIdBookerAndItemIdAndStatusOrderByEndAsc(Mockito.anyLong(), Mockito.anyLong(), Mockito.any())).thenReturn(Optional.empty());
        assertThrows(OwnerHasNotItemException.class, () -> itemService.postCommentByItemId(commentDto));
    }

    @Test
    public void postCommentNullBookingAndEndAfterLocalDateNow() {
        Booking booking = Booking.builder().end(LocalDateTime.now().plusHours(2)).build();
        CommentDto commentDto = CommentDto.builder().itemId(1).text("Text").id(1).build();
        when(bookingRepository.findFirstByIdBookerAndItemIdAndStatusOrderByEndAsc(Mockito.anyLong(), Mockito.anyLong(), Mockito.any())).thenReturn(Optional.ofNullable(booking));
        assertThrows(ChangeDeprecated.class, () -> itemService.postCommentByItemId(commentDto));
    }

    @Test
    public void postCommentSuccess() {
        Booking booking = Booking.builder().end(LocalDateTime.now().minusHours(2)).build();
        CommentDto commentDto = CommentDto.builder().itemId(1).text("Text").id(1).build();
        when(bookingRepository.findFirstByIdBookerAndItemIdAndStatusOrderByEndAsc(Mockito.anyLong(), Mockito.anyLong(), Mockito.any())).thenReturn(Optional.ofNullable(booking));
        when(commentRepository.save(any())).thenReturn(Comment.builder().text("test").author(User.builder().id(1L).name("Name").build()).itemId(1L).created(LocalDateTime.now().minusHours(2)).build());
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(User.builder().id(1).build()));
        CommentAnswerDto commentAnswerDto = itemService.postCommentByItemId(commentDto);
        assertNotNull(commentAnswerDto);
    }

    @Test
    public void mappersTest() {
        CommentDtoMapper commentDtoMapper = new CommentDtoMapper();
        assertNotNull(commentDtoMapper);
        Comment comment = Comment.builder().text("test").author(User.builder().id(1L).name("Name").build()).itemId(1L).created(LocalDateTime.now().minusHours(2)).build();
        CommentAnswerDto commentAnswerDto = CommentDtoMapper.toAnswerDto(comment);
        assertNotNull(commentAnswerDto);
        Comment commentFromAnswerDto = CommentDtoMapper.fromAnswerDto(commentAnswerDto);
        assertNotNull(commentFromAnswerDto);
        CommentDto commentDto = CommentDtoMapper.toDto(comment);
        assertNotNull(commentDto);
        Comment comment1 = CommentDtoMapper.fromDto(commentDto);
        assertNotNull(comment1);
    }
}