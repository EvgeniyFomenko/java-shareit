package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentAnswerDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final UserService userService;

    @GetMapping
    public List<ItemDto> getAll(@RequestHeader("X-Sharer-User-Id") long userId, @RequestParam(defaultValue = "0", required = false) Integer from, @RequestParam(defaultValue = "10", required = false) Integer size) {
        return itemService.getAll(userId, from, size);
    }

    @GetMapping("/{id}")
    public ItemDto getById(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long id) {
        return itemService.get(id, userId);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long id) {
        itemService.delete(id);
    }

    @PatchMapping("/{id}")
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") long userId, @RequestBody ItemDto itemDto, @PathVariable long id) {
        itemDto.setId(id);
        return itemService.update(itemDto, userId);
    }

    @PostMapping
    public ItemDto add(@RequestHeader("X-Sharer-User-Id") long userId, @RequestBody @Valid ItemDto itemDto) {

        return itemService.create(itemDto, userId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text, @RequestParam(defaultValue = "0", required = false) Integer from, @RequestParam(defaultValue = "10", required = false) Integer size) {
        return itemService.search(text, from, size);
    }

    @PostMapping("{itemId}/comment")
    public CommentAnswerDto getCommentByItemId(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long itemId, @Valid @RequestBody CommentDto commentDto) {
        commentDto.setItemId(itemId);
        commentDto.setId(userId);
        return itemService.postCommentByItemId(commentDto);
    }

}
