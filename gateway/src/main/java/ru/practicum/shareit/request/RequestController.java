package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.BadArgumentsPaginationException;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Objects;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
@Slf4j
public class RequestController {
    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> postRequests(@RequestHeader("X-Sharer-User-Id") long userId, @RequestBody @Valid ItemRequestDto requestDto) {
        requestDto.setRequester(userId);
        log.info("post requests with userId={}, requestDescription={}",userId,requestDto.getDescription());
        return requestClient.postRequests(userId, requestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getRequests(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("get request with userId={}", userId);
        return requestClient.getRequests(userId);

    }


    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable int requestId) {
        log.info("get request with userId={}, requestId={}", userId, requestId);
        return requestClient.getRequestById(userId, requestId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequests(@RequestHeader("X-Sharer-User-Id") long userId, @RequestParam(required = false) Integer from, @RequestParam(required = false) Integer size) {
        if (Objects.isNull(from) || Objects.isNull(size)) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
        }

        if (from < 0 || size <= 0) {
            throw new BadArgumentsPaginationException("такой страницы не существует");
        }
        log.info("get all requests with userId={}, from={}, size={}",userId,from,size);
        return requestClient.getAllRequests(userId, from, size);
    }

}
