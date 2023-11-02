package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Validated
@Slf4j
public class ItemController {
    private final ItemClient itemClient;

    @GetMapping
    public ResponseEntity<Object> getAll(@RequestHeader("X-Sharer-User-Id") long userId, @RequestParam(defaultValue = "0", required = false) Integer from, @RequestParam(defaultValue = "10", required = false) Integer size) {
        log.info("Get items with, userId={}, from={}, size={}", userId, from, size);
        return itemClient.getAll(userId, from, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long id) {
        log.info("Get item with, userId={}, itemId={}", userId,id);
        return itemClient.getById(userId, id);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long id) {
        log.info("Delete item with, userId={}, itemId={}", userId,id);
        itemClient.deleteById(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@RequestHeader("X-Sharer-User-Id") long userId, @RequestBody ItemDto itemDto, @PathVariable long id) {
        itemDto.setId(id);
        log.info("Patch item with, userId={}, itemId={}, itemAvailable={}, itemName={}, getDescription={}", userId,id,itemDto.getAvailable(),itemDto.getName(),itemDto.getDescription());
        return itemClient.update(userId, itemDto, id);
    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestHeader("X-Sharer-User-Id") long userId, @RequestBody @Valid ItemDto itemDto) {
        log.info("Add item with userId={},itemAvailable={}, itemName={}, getDescription={} ",userId,itemDto.getAvailable(),itemDto.getName(),itemDto.getDescription());
        return itemClient.add(userId, itemDto);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestParam String text, @RequestParam(defaultValue = "0", required = false) Integer from, @RequestParam(defaultValue = "10", required = false) Integer size) {
        log.info("Search item with, text={}, form={}, size={}",text, from,size);
        return itemClient.search(text, from, size);
    }

    @PostMapping("{itemId}/comment")
    public ResponseEntity<Object> getCommentByItemId(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long itemId, @Valid @RequestBody CommentDto commentDto) {
        log.info("Post item with, userId={}, itemId={}, commentText={}",userId, itemId, commentDto.getText());
        return itemClient.getCommentByItemId(userId, itemId, commentDto);
    }
}
