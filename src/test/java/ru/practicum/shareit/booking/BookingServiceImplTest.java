package ru.practicum.shareit.booking;

import org.jeasy.random.EasyRandom;
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
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingForItemDto;
import ru.practicum.shareit.booking.dto.BookingInputStatusDto;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repositories.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;


@SpringBootTest
class BookingServiceImplTest {
    @Mock
    BookingRepository bookingRepository;
    @Mock
    ItemRepository itemRepository;
    @Mock
    UserRepository userRepository;
    @InjectMocks
    BookingServiceImpl bookingService;
    @Test
    public void testGetBooking() {
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusDays(2);
        Item item = Item.builder()
                .name("item01")
                .booking(null)
                .nextBooking(null)
                .lastBooking(null)
                .idOwner(1)
                .id(1L)
                .description("description")
                .available(true)
                .requestId(1L)
                .build();
        Booking booking = Booking.builder()
                .id(1)
                .status(StatusBooking.APPROVED)
                .end(startDate)
                .start(endDate)
                .item(item)
                .idBooker(1)
                .build();

        BookingInputStatusDto bookingInputStatusDtoTest = BookingMapper.toBookingInputStatusDto(1,"true",1);
        Assertions.assertNotNull(bookingInputStatusDtoTest);
        BookingMapper bookingMapper = new BookingMapper();
        Assertions.assertNotNull(bookingMapper);
        BookingItemDto bookingItemDto = BookingItemMapper.toDto(booking);
        Assertions.assertNotNull(bookingItemDto);
        BookingItemMapper bookingItemMapper = new BookingItemMapper();
        Assertions.assertNotNull(bookingItemMapper);
        BookingForItemMapper bookingForItemDto = new BookingForItemMapper();
        Assertions.assertNotNull(bookingForItemDto);
        BookingForItemDto bfid = BookingForItemMapper.toDto(booking);
        BookingForItemDto bfidNull = BookingForItemMapper.toDto(null);
        Assertions.assertNull(bfidNull);
        Assertions.assertNotNull(bfid);
        when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(booking));
        User userOne = User.builder().id(1).build();

        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(userOne));

        BookingInputStatusDto bookingInputStatusDto = BookingInputStatusDto.builder()
                .bookingId(1)
                .userId(1)
                .isApprove("CURRENT").build();
        BookingDto bookingDto = BookingDto.builder().id(1).status(StatusBooking.APPROVED).build();

        Assertions.assertEquals(bookingDto.getId(), bookingService.get(bookingInputStatusDto).getId());


        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(UserNotFoundException.class,()->bookingService.get(bookingInputStatusDto));


        when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(BookingNotFoundException.class,()->bookingService.get(bookingInputStatusDto));

        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(userOne));
        booking.setIdBooker(99);
        booking.getItem().setIdOwner(3);
        when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(booking));

        Assertions.assertThrows(NotFoundStateException.class,()->bookingService.get(bookingInputStatusDto));

        booking.getItem().setIdOwner(1);
        when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(booking));
        Assertions.assertNotNull(bookingService.get(bookingInputStatusDto));
    }

    @Test
    public void testCreateBooking(){
        LocalDateTime startDate = LocalDateTime.now().plusDays(1);
        LocalDateTime endDate = startDate.plusDays(2);
        Item item = Item.builder()
                .name("item01")
                .booking(null)
                .nextBooking(null)
                .lastBooking(null)
                .idOwner(1)
                .id(1L)
                .description("description")
                .available(true)
                .requestId(1L)
                .build();
        Booking booking = Booking.builder()
                .id(1)
                .status(StatusBooking.APPROVED)
                .end(startDate)
                .start(endDate)
                .item(item)
                .idBooker(2)
                .build();
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item));
        User userOne = User.builder().id(1).build();
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(userOne));
        when(bookingRepository.save(Mockito.any(Booking.class))).thenReturn(booking);
        BookingItemDto bookingItemDto = BookingItemDto.builder()
                .itemId(1)
                .end(endDate)
                .start(startDate)
                .build();
        BookingDto bookingDto = BookingDto.builder()
                .id(0)
                .status(StatusBooking.APPROVED)
                .start(startDate)
                .end(endDate)
                .item(item)
                .booker(userOne)
                .build();

        long userId = 2L;
        BookingDto bookingDtoTest = bookingService.create(bookingItemDto, userId);

        Assertions.assertEquals(bookingDto.getId(), bookingDtoTest.getId());

        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(ItemNotFoundException.class,()-> bookingService.create(bookingItemDto, userId));

        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item));
        item.setAvailable(false);
        Assertions.assertThrows(ItemUnavailableException.class,()-> bookingService.create(bookingItemDto, userId));

        item.setIdOwner(2);
        Assertions.assertThrows(NotFoundStateException.class,()-> bookingService.create(bookingItemDto, userId));

        bookingItemDto.setEnd(startDate);
        Assertions.assertThrows(BookingTimeException.class,()-> bookingService.create(bookingItemDto, userId));
        bookingItemDto.setStart(LocalDateTime.now().minusHours(2));
        Assertions.assertThrows(BookingTimeException.class,()-> bookingService.create(bookingItemDto, userId));
        bookingItemDto.setStart(endDate);
        Assertions.assertThrows(BookingTimeException.class,()-> bookingService.create(bookingItemDto, userId));


    }

    @Test
    public void testUpdateBookingStatusById() {
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusDays(2);
        Item item = Item.builder()
                .name("item01")
                .booking(null)
                .nextBooking(null)
                .lastBooking(null)
                .idOwner(1)
                .id(1L)
                .description("description")
                .available(true)
                .requestId(1L)
                .build();
        Booking preSaveBooking = Booking.builder()
                .id(1)
                .status(StatusBooking.WAITING)
                .end(startDate)
                .start(endDate)
                .item(item)
                .idBooker(1)
                .build();
        Booking afterSaveBooking = Booking.builder()
                .id(1)
                .status(StatusBooking.APPROVED)
                .end(startDate)
                .start(endDate)
                .item(item)
                .idBooker(1)
                .build();
        when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(preSaveBooking));
        when(bookingRepository.save(Mockito.any())).thenReturn(afterSaveBooking);
        BookingDto bookingDto = BookingMapper.toBookingDto(afterSaveBooking);
        BookingInputStatusDto bookingInputStatusDto = BookingInputStatusDto.builder()
                .bookingId(1)
                .isApprove("true")
                .userId(1)
                .build();

        Assertions.assertEquals(bookingDto.toString(),bookingService.updateBookingStatusById(bookingInputStatusDto).toString());
        preSaveBooking.getItem().setIdOwner(3);
        preSaveBooking.setStatus(StatusBooking.WAITING);
        Assertions.assertThrows(NotFoundStateException.class,()->bookingService.updateBookingStatusById(bookingInputStatusDto));
        bookingInputStatusDto.setIsApprove("false");
        preSaveBooking.getItem().setIdOwner(1);
        Assertions.assertEquals(StatusBooking.REJECTED,bookingService.updateBookingStatusById(bookingInputStatusDto).getStatus());
        preSaveBooking.getItem().setIdOwner(3);
        Assertions.assertEquals(StatusBooking.CANCELED,bookingService.updateBookingStatusById(bookingInputStatusDto).getStatus());



        preSaveBooking.getItem().setIdOwner(3);
        preSaveBooking.setIdBooker(99);
        preSaveBooking.setStatus(StatusBooking.WAITING);
        Assertions.assertThrows(NotFoundStateException.class,()->bookingService.updateBookingStatusById(bookingInputStatusDto));


        preSaveBooking.setStatus(StatusBooking.APPROVED);
        Assertions.assertThrows(ChangeDeprecated.class,()->bookingService.updateBookingStatusById(bookingInputStatusDto).toString());


        bookingInputStatusDto.setIsApprove("sdf");
        Assertions.assertThrows(NotFoundStateException.class,()-> bookingService.updateBookingStatusById(bookingInputStatusDto));

        when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(BookingNotFoundException.class,()->bookingService.updateBookingStatusById(bookingInputStatusDto));
        Assertions.assertNull(bookingService.update(preSaveBooking));
        bookingService.delete(1);
    }

    @Test
    public void getAllBookingForUserTest(){
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusDays(2);
        User userOne = User.builder().id(1).build();
        Booking bookingDto = Booking.builder()
                .id(1)
                .status(StatusBooking.APPROVED)
                .start(startDate)
                .end(endDate)
                .build();
        Booking bookingDtoTwo = Booking.builder()
                .id(1)
                .status(StatusBooking.APPROVED)
                .start(startDate)
                .end(endDate)
                .build();
        List<Booking> listBooking = new ArrayList<>();
        listBooking.add(bookingDto);
        listBooking.add(bookingDtoTwo);
        Page<Booking> page = new PageImpl(listBooking, PageRequest.of(1, 5, Sort.Direction.ASC, "id"),listBooking.size());
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(userOne));
        when(bookingRepository.findAllByIdBookerOrderByIdDesc(Mockito.anyLong(),Mockito.any()))
                .thenReturn(page);
        when(bookingRepository.findAllByIdBookerAndStatus(Mockito.anyLong(),Mockito.any(),Mockito.any()))
                .thenReturn(page);
        when(bookingRepository.findAllByIdBookerAndStartIsAfterOrderByStartDesc(Mockito.anyLong(),Mockito.any(),Mockito.any()))
                .thenReturn(page);
        when(bookingRepository.findAllByIdBookerAndStartIsBeforeAndEndIsAfter(Mockito.anyLong(),Mockito.any(),Mockito.any(),Mockito.any()))
                .thenReturn(page);
        when(bookingRepository.findAllByIdBookerAndStatusAndStartIsBeforeAndEndIsBeforeOrderByIdDesc(Mockito.anyLong(),Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any()))
                .thenReturn(page);

        List<BookingDto> listAll = bookingService.getAllBookingForUser( BookingInputStatusDto.builder().isApprove("ALL").userId(1).bookingId(1).build(),3,23);
        Assertions.assertEquals(2,listAll.size());

        List<BookingDto> listFuture = bookingService.getAllBookingForUser( BookingInputStatusDto.builder().isApprove("FUTURE").userId(1).bookingId(1).build(),3,23);
        Assertions.assertEquals(2,listFuture.size());

            List<BookingDto> listWaiting = bookingService.getAllBookingForUser( BookingInputStatusDto.builder().isApprove("WAITING").userId(1).bookingId(1).build(),3,23);
        Assertions.assertEquals(2,listWaiting.size());
        ;
        List<BookingDto> listRejected = bookingService.getAllBookingForUser( BookingInputStatusDto.builder().isApprove("REJECTED").userId(1).bookingId(1).build(),3,23);
        Assertions.assertEquals(2,listRejected.size());

        List<BookingDto> listCurrent = bookingService.getAllBookingForUser( BookingInputStatusDto.builder().isApprove("CURRENT").userId(1).bookingId(1).build(),3,23);
        Assertions.assertEquals(2,listCurrent.size());

        List<BookingDto> listPast = bookingService.getAllBookingForUser( BookingInputStatusDto.builder().isApprove("PAST").userId(1).bookingId(1).build(),3,23);
        Assertions.assertEquals(2,listPast.size());
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(UserNotFoundException.class,()->bookingService.getAllBookingForUser( BookingInputStatusDto.builder().isApprove("PAST").userId(99).bookingId(1).build(),0,12));
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(userOne));
        Assertions.assertThrows(UnknownStatus.class,()->bookingService.getAllBookingForUser( BookingInputStatusDto.builder().isApprove("adf").userId(1).bookingId(1).build(),3,23));
        Assertions.assertThrows(BadArgumentsPaginationException.class,()-> bookingService.getAllBookingForUser( BookingInputStatusDto.builder().isApprove("PAST").userId(1).bookingId(1).build(),0,0));
        Assertions.assertThrows(BadArgumentsPaginationException.class,()-> bookingService.getAllBookingForUser( BookingInputStatusDto.builder().isApprove("PAST").userId(1).bookingId(1).build(),1,0));
        Assertions.assertThrows(BadArgumentsPaginationException.class,()-> bookingService.getAllBookingForUser( BookingInputStatusDto.builder().isApprove("PAST").userId(1).bookingId(1).build(),-1,0));


        List<BookingDto> listEmpty = bookingService.getAllBookingForUser( BookingInputStatusDto.builder().isApprove("PAST").userId(1).bookingId(1).build(),null,null);
        Assertions.assertEquals(0,listEmpty.size());
        listEmpty = bookingService.getAllBookingForUser( BookingInputStatusDto.builder().isApprove("PAST").userId(1).bookingId(1).build(),null,1);
        Assertions.assertEquals(0,listEmpty.size());
        listEmpty = bookingService.getAllBookingForUser( BookingInputStatusDto.builder().isApprove("PAST").userId(1).bookingId(1).build(),1,null);
        Assertions.assertEquals(0,listEmpty.size());
    }

    @Test
    public void getAllBookingForOwnerItemTest(){
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusDays(2);
        EasyRandom easyRandom = new EasyRandom();
        User userOne = User.builder().id(1).build();
        Booking bookingDto = Booking.builder()
                .id(1)
                .status(StatusBooking.APPROVED)
                .start(startDate)
                .end(endDate)
                .build();
        Booking bookingDtoTwo = Booking.builder()
                .id(1)
                .status(StatusBooking.APPROVED)
                .start(startDate)
                .end(endDate)
                .build();
        List<Booking> listBooking = new ArrayList<>();
        listBooking.add(bookingDto);
        listBooking.add(bookingDtoTwo);
        Page<Booking> page = new PageImpl(listBooking, PageRequest.of(1, 5, Sort.Direction.ASC, "id"),listBooking.size());
        when(itemRepository.countItemByIdOwner(Mockito.anyLong())).thenReturn(1);
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(userOne));
        when(bookingRepository.findAllByItemIdOwnerOrderByIdDesc(Mockito.anyLong(),Mockito.any()))
                .thenReturn(page);
        when(bookingRepository.findAllByItemIdOwnerAndStartIsAfterOrderByStartDesc(Mockito.anyLong(),Mockito.any(),Mockito.any()))
                .thenReturn(page);
        when(bookingRepository.findAllByItemIdOwnerAndStatus(Mockito.anyLong(),Mockito.any(),Mockito.any()))
                .thenReturn(page);
        when(bookingRepository.findAllByItemIdOwnerAndStartIsBeforeAndEndIsAfter(Mockito.anyLong(),Mockito.any(),Mockito.any(),Mockito.any()))
                .thenReturn(page);
        when(bookingRepository.findAllByItemIdOwnerAndStatusAndStartIsBeforeAndEndIsBeforeOrderByIdDesc(Mockito.anyLong(),Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any()))
                .thenReturn(page);

        List<BookingDto> listAll = bookingService.getAllBookingForOwnerItem( BookingInputStatusDto.builder().isApprove("ALL").userId(1).bookingId(1).build(),3,23);
        Assertions.assertEquals(2,listAll.size());

        List<BookingDto> listFuture = bookingService.getAllBookingForOwnerItem( BookingInputStatusDto.builder().isApprove("FUTURE").userId(1).bookingId(1).build(),3,23);
        Assertions.assertEquals(2,listFuture.size());

        List<BookingDto> listWaiting = bookingService.getAllBookingForOwnerItem( BookingInputStatusDto.builder().isApprove("WAITING").userId(1).bookingId(1).build(),3,23);
        Assertions.assertEquals(2,listWaiting.size());
        ;
        List<BookingDto> listRejected = bookingService.getAllBookingForOwnerItem( BookingInputStatusDto.builder().isApprove("REJECTED").userId(1).bookingId(1).build(),3,23);
        Assertions.assertEquals(2,listRejected.size());

        List<BookingDto> listCurrent = bookingService.getAllBookingForOwnerItem( BookingInputStatusDto.builder().isApprove("CURRENT").userId(1).bookingId(1).build(),3,23);
        Assertions.assertEquals(2,listCurrent.size());

        List<BookingDto> listPast = bookingService.getAllBookingForOwnerItem( BookingInputStatusDto.builder().isApprove("PAST").userId(1).bookingId(1).build(),3,23);
        Assertions.assertEquals(2,listPast.size());

        Assertions.assertThrows(UnknownStatus.class,()->bookingService.getAllBookingForOwnerItem( BookingInputStatusDto.builder().isApprove("adf").userId(1).bookingId(1).build(),0,23));
        Assertions.assertThrows(BadArgumentsPaginationException.class,()-> bookingService.getAllBookingForOwnerItem( BookingInputStatusDto.builder().isApprove("PAST").userId(1).bookingId(1).build(),0,0));
        Assertions.assertThrows(BadArgumentsPaginationException.class,()-> bookingService.getAllBookingForOwnerItem( BookingInputStatusDto.builder().isApprove("PAST").userId(1).bookingId(1).build(),1,-1));
        Assertions.assertThrows(BadArgumentsPaginationException.class,()-> bookingService.getAllBookingForOwnerItem( BookingInputStatusDto.builder().isApprove("PAST").userId(1).bookingId(1).build(),-1,1));

        List<BookingDto> listEmpty = bookingService.getAllBookingForOwnerItem( BookingInputStatusDto.builder().isApprove("PAST").userId(1).bookingId(1).build(),null,null);
        Assertions.assertEquals(0,listEmpty.size());
         listEmpty = bookingService.getAllBookingForOwnerItem( BookingInputStatusDto.builder().isApprove("PAST").userId(1).bookingId(1).build(),0,null);
        Assertions.assertEquals(0,listEmpty.size());
         listEmpty = bookingService.getAllBookingForOwnerItem( BookingInputStatusDto.builder().isApprove("PAST").userId(1).bookingId(1).build(),null,0);
        Assertions.assertEquals(0,listEmpty.size());

        when(itemRepository.countItemByIdOwner(Mockito.anyLong())).thenReturn(0);
        Assertions.assertThrows(UnknownStatus.class,()-> bookingService.getAllBookingForOwnerItem( BookingInputStatusDto.builder().isApprove("PAST").userId(1).bookingId(1).build(),3,10));

    }
}