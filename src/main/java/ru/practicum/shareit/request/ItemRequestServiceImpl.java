package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.BadArgumentsPaginationException;
import ru.practicum.shareit.exception.BookingNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.entity.ItemRequest;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;

    @Override
    public RequestDto get(long userId, int id) {
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
        return RequestMapper.toRequestDto(itemRequestRepository.findById(Integer.toUnsignedLong(id)).orElseThrow(() -> new BookingNotFoundException("Бранирование с таким айди не найдено")));
    }

    @Override
    public ItemRequestDto create(ItemRequestDto itemRequestDto) {
        userRepository.findById(itemRequestDto.getRequester()).orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
        ItemRequest itemRequest = RequestMapper.fromDto(itemRequestDto);
        itemRequest.setCreated(LocalDateTime.now());
        return RequestMapper.toDto(itemRequestRepository.save(itemRequest));
    }


    @Override
    public List<ItemRequest> getAll(long userId, Integer from, Integer size) {
        if (Objects.isNull(from) || Objects.isNull(size)) {
            return Collections.emptyList();
        }

        if (from < 0 || size <= 0) {
            throw new BadArgumentsPaginationException("такой страницы не существует");
        }

        return itemRequestRepository.findAllByRequesterIdNot(userId, PageRequest.of(from > 0 ? from / size : 0, size, Sort.Direction.ASC, "id")).getContent();


    }

    @Override
    public List<ItemRequest> getAll(long userId) {
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
        return itemRequestRepository.findAllByRequesterId(userId);

    }
}
