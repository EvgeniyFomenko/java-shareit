package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInputStatusDto;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repositories.ItemRepository;
import ru.practicum.shareit.user.UserRepository;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
@Repository
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public BookingDto get(BookingInputStatusDto bookingInputStatusDto) {
        long bookingId = bookingInputStatusDto.getBookingId();
        long userId = bookingInputStatusDto.getUserId();
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new BookingNotFoundException("Бранирование не найдено"));

        validate(booking, userId);
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public BookingDto create(@Valid BookingItemDto bookingItemDto, long userId) {
        Booking booking = BookingItemMapper.fromDto(bookingItemDto);
        booking.setIdBooker(userId);

        if (booking.getStart().isAfter(booking.getEnd()) || booking.getStart().isBefore(LocalDateTime.now())) {
            throw new BookingTimeException("время бронирования не может быть в прошлом");
        }

        if (booking.getStart().equals(booking.getEnd())) {
            throw new BookingTimeException("время начала и конца бронинорвания не может быть равными друг другу");
        }

        Item item1 = itemRepository.findById(booking.getItem().getId()).orElseThrow(() -> new ItemNotFoundException("item not found"));
        if (booking.getIdBooker() == item1.getIdOwner()) {
            throw new NotFoundStateException("Владелец не может бронировать свою вещь ");
        }
        if (item1.getAvailable().equals(false)) {
            throw new ItemUnavailableException("item unavailable");
        }

        userRepository.findById(booking.getIdBooker()).orElseThrow();
        Booking booking1 = bookingRepository.save(booking);
        booking1.setItem(item1);
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public Booking update(Booking booking) {
        return null;
    }

    public BookingDto updateBookingStatusById(BookingInputStatusDto bookingInputStatusDto) {
        Booking booking = bookingRepository.findById(bookingInputStatusDto.getBookingId()).orElseThrow(() -> new BookingNotFoundException("Бранирование не найдено"));
        String isApprove = bookingInputStatusDto.getIsApprove();
        if (!isApprove.contains("false")) {
            if (!isApprove.contains("true")) {
                throw new NotFoundStateException("не известное значение isApproved");
            }
        }
        long userId = bookingInputStatusDto.getUserId();

        if (booking.getStatus().equals(StatusBooking.APPROVED)) {
            throw new ChangeDeprecated("После подтверждения изменения в обратную сторону запрещены");
        }

        if (Boolean.parseBoolean(isApprove)) {
            if (Objects.equals(booking.getItem().getIdOwner(), userId)) {
                booking.setStatus(StatusBooking.APPROVED);
                bookingRepository.save(booking);
                return BookingMapper.toBookingDto(booking);
            } else {
                throw new NotFoundStateException("Пользователь запросивший изменение не является владельцем вещи");
            }
        } else {
            if (Objects.equals(booking.getItem().getIdOwner(), userId)) {
                booking.setStatus(StatusBooking.REJECTED);
            } else if (Objects.equals(booking.getIdBooker(), userId)) {
                booking.setStatus(StatusBooking.CANCELED);
            } else {
                throw new NotFoundStateException("Пользователь запросивший изменение не является владельцем вещи");
            }
            bookingRepository.save(booking);
            return BookingMapper.toBookingDto(booking);
        }
    }

    public List<BookingDto> getAllBookingForUser(BookingInputStatusDto bookingInputStatusDto, Integer from, Integer size) {
        if (Objects.isNull(from) || Objects.isNull(size)) {
            return Collections.emptyList();
        }

        if (from < 0 || size <= 0) {
            throw new BadArgumentsPaginationException("такой страницы не существует");
        }

        long userId = bookingInputStatusDto.getUserId();
        PageRequest pageRequest = PageRequest.of(from > 0 ? from / size : 0, size, Sort.Direction.ASC, "id");
        State state;
        try {
            state = State.valueOf(bookingInputStatusDto.getIsApprove());
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new UnknownStatus("UNSUPPORTED STATUS");
        }

        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("нет пользователя с таким id"));
        if (state.equals(State.ALL)) {
            return bookingRepository.findAllByIdBookerOrderByIdDesc(userId, pageRequest).stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
        } else if (state.equals(State.FUTURE)) {
            return bookingRepository.findAllByIdBookerAndStartIsAfterOrderByStartDesc(userId, LocalDateTime.now(), pageRequest).stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
        } else if (state.equals(State.WAITING)) {
            return bookingRepository.findAllByIdBookerAndStatus(userId, StatusBooking.WAITING, pageRequest).stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
        } else if (state.equals(State.REJECTED)) {
            return bookingRepository.findAllByIdBookerAndStatus(userId, StatusBooking.REJECTED, pageRequest).stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
        } else if (state.equals(State.CURRENT)) {
            return bookingRepository.findAllByIdBookerAndStartIsBeforeAndEndIsAfter(userId, LocalDateTime.now(), LocalDateTime.now(), pageRequest).stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
        } else {
            return bookingRepository.findAllByIdBookerAndStatusAndStartIsBeforeAndEndIsBeforeOrderByIdDesc(userId, StatusBooking.APPROVED, LocalDateTime.now(), LocalDateTime.now(), pageRequest).stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
        }
    }

    @Override
    public void delete(int id) {

    }

    private void validate(Booking booking, long userId) {
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
        if (Objects.equals(booking.getIdBooker(), userId) || Objects.equals(booking.getItem().getIdOwner(), userId)) {
            return;
        }
        throw new NotFoundStateException("Нет доступа для просмотра");
    }

    public List<BookingDto> getAllBookingForOwnerItem(BookingInputStatusDto booking, Integer from, Integer size) {
        if (Objects.isNull(from) || Objects.isNull(size)) {
            return Collections.emptyList();
        }

        if (from < 0 || size <= 0) {
            throw new BadArgumentsPaginationException("такой страницы не существует");
        }
        PageRequest pageRequest = PageRequest.of(from > 0 ? from / size : 0, size, Sort.Direction.ASC, "id");
        State state;
        try {
            state = State.valueOf(booking.getIsApprove());
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new UnknownStatus("UNSUPPORTED STATUS");
        }
        long userId = booking.getUserId();
        int countItemUser = itemRepository.countItemByIdOwner(userId);
        int zeroItem = 0;
        if (countItemUser > zeroItem) {

            if (state.equals(State.ALL)) {
                return bookingRepository.findAllByItemIdOwnerOrderByIdDesc(userId, pageRequest).stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
            } else if (state.equals(State.FUTURE)) {
                return bookingRepository.findAllByItemIdOwnerAndStartIsAfterOrderByStartDesc(userId, LocalDateTime.now(), pageRequest).stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
            } else if (state.equals(State.WAITING)) {
                return bookingRepository.findAllByItemIdOwnerAndStatus(userId, StatusBooking.WAITING, pageRequest).stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
            } else if (state.equals(State.REJECTED)) {
                return bookingRepository.findAllByItemIdOwnerAndStatus(userId, StatusBooking.REJECTED, pageRequest).stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
            } else if (state.equals(State.CURRENT)) {
                return bookingRepository.findAllByItemIdOwnerAndStartIsBeforeAndEndIsAfter(userId, LocalDateTime.now(), LocalDateTime.now(), pageRequest).stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
            } else {
                return bookingRepository.findAllByItemIdOwnerAndStatusAndStartIsBeforeAndEndIsBeforeOrderByIdDesc(userId, StatusBooking.APPROVED, LocalDateTime.now(), LocalDateTime.now(), pageRequest).stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
            }
        } else {
            throw new UnknownStatus("UNSUPPORTED STATUS");
        }
    }
}
