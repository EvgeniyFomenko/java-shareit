package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingForItemMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.StatusBooking;
import ru.practicum.shareit.booking.dto.BookingForItemDto;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.dto.CommentAnswerDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repositories.CommentRepository;
import ru.practicum.shareit.item.repositories.ItemRepository;
import ru.practicum.shareit.user.UserRepository;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Override
    public List<ItemDto> getAll(long userId, Integer from, Integer size) {

        PageRequest pageRequest = PageRequest.of(from > 0 ? from / size : 0, size, Sort.Direction.ASC, "id");

        return itemRepository.findAllByIdOwner(userId, pageRequest).stream()
                .peek(e -> {
                    setLastFutureBooking(e, userId);
                    setCommentsInItem(e);
                })
                .map(ItemDtoMapper::mapToDto).collect(Collectors.toList());
    }

    private void setCommentsInItem(Item item) {
        List<CommentAnswerDto> comments = commentRepository.findAllByItemId(item.getId()).get().stream().map(CommentDtoMapper::toAnswerDto).collect(Collectors.toList());
        log.info(comments.toString());
        item.setComments(comments);
    }

    @Override
    public ItemDto get(long itemId, long userId) {
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException("Вещ с таким id не зарегестрирована"));
        setCommentsInItem(item);
        setLastFutureBooking(item, userId);
        return ItemDtoMapper.mapToDto(item);
    }

    private void setLastFutureBooking(Item item, long userId) {
        BookingForItemDto lastBooking = BookingForItemMapper.toDto(bookingRepository.findFirstByItemIdOwnerAndStartIsBeforeAndStatusOrderByStartDesc(userId, LocalDateTime.now(), StatusBooking.APPROVED));
        BookingForItemDto futureBooking = BookingForItemMapper.toDto(bookingRepository.findFirstByItemIdOwnerAndStartIsAfterAndStatusOrderByStartAsc(userId, LocalDateTime.now(), StatusBooking.APPROVED));
        if (Objects.nonNull(lastBooking)) {
            if (item.getId().equals(lastBooking.getItemId())) {
                item.setLastBooking(lastBooking);
            }
        }

        if (Objects.nonNull(futureBooking)) {
            if (item.getId().equals(futureBooking.getItemId())) {
                item.setNextBooking(futureBooking);
            }
        }
    }


    @Override
    public ItemDto create(@Valid ItemDto itemDto, long userId) {
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
        itemDto.setOwnerId(userId);

        return ItemDtoMapper.mapToDto(itemRepository.save(ItemDtoMapper.mapToItem(itemDto)));
    }


    @Override
    public ItemDto update(@Valid ItemDto itemDto, long userId) {
        Item item = ItemDtoMapper.mapToItem(itemDto);

        validation(userId, item.getId());

        Item item1 = ItemDtoMapper.mapToItem(get(item.getId(), userId));
        if (Objects.nonNull(item.getName())) {
            item1.setName(item.getName());
        }

        if (Objects.nonNull(item.getDescription())) {
            item1.setDescription(item.getDescription());
        }

        if (Objects.nonNull(item.getAvailable())) {
            item1.setAvailable(item.getAvailable());
        }

        return ItemDtoMapper.mapToDto(itemRepository.save(item1));
    }

    private void validation(long userId, Long itemId) {
        if (userId != itemRepository.findById(itemId).orElseThrow().getIdOwner()) {
            throw new AccessDenideException("Пльзователю запрещено менять этот item");
        }
    }

    @Override
    public void delete(long id) {
        itemRepository.deleteById(id);
    }

    @Override
    public List<ItemDto> search(String text, Integer from, Integer size) {

        List<Item> items = itemRepository.findAllByDescriptionContainingIgnoreCaseAndAvailableTrue(text,PageRequest.of(from > 0 ? from / size : 0, size, Sort.Direction.ASC, "id")).getContent();

        return items.stream().filter(e -> e.getAvailable().equals(true)).map(ItemDtoMapper::mapToDto).collect(Collectors.toList());
    }

    @Override
    public CommentAnswerDto postCommentByItemId(@Valid CommentDto comment) {
        Booking booking = bookingRepository.findFirstByIdBookerAndItemIdAndStatusOrderByEndAsc(comment.getId(), comment.getItemId(), StatusBooking.APPROVED).orElseThrow(() -> new OwnerHasNotItemException("Пользователь не бронировал эту вещь"));

        if (booking.getEnd().isAfter(LocalDateTime.now())) {
            throw new ChangeDeprecated("Пользователю запрещено оставлять комменты до окончания аренды");
        }

        Comment comment1 = commentRepository.save(CommentDtoMapper.fromDto(comment));
        comment1.setAuthor(userRepository.findById(comment1.getAuthor().getId()).get());
        return CommentDtoMapper.toAnswerDto(comment1);
    }
}
