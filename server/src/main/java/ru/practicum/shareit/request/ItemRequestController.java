package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.entity.ItemRequest;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto postRequests(@RequestHeader("X-Sharer-User-Id") long userId, @RequestBody @Valid ItemRequestDto requestDto) {
        requestDto.setRequester(userId);
        return itemRequestService.create(requestDto);
    }

    @GetMapping
    public List<ItemRequest> getRequests(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemRequestService.getAll(userId);

    }

    @GetMapping("/{requestId}")
    public RequestDto getRequestById(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable int requestId) {
        return itemRequestService.get(userId, requestId);
    }

    @GetMapping("/all")
    public List<ItemRequest> getAllRequests(@RequestHeader("X-Sharer-User-Id") long userId, @RequestParam(required = false) Integer from, @RequestParam(required = false) Integer size) {
        log.info("getAll from server user={} from={} size={}",userId,from,size);
        return itemRequestService.getAll(userId, from, size);
    }


}
