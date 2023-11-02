package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentAnswerDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

public class CommentDtoMapper {
    public static Comment fromDto(CommentDto commentDto) {
        return Comment.builder()
                .author(User.builder().id(commentDto.getId()).build())
                .text(commentDto.getText())
                .itemId(commentDto.getItemId())
                .created(LocalDateTime.now())
                .build();
    }

    public static CommentDto toDto(Comment comment) {
        return CommentDto.builder()
                .text(comment.getText())
                .id(comment.getAuthor().getId())
                .itemId(comment.getItemId()).build();
    }

    public static CommentAnswerDto toAnswerDto(Comment comment) {
        return CommentAnswerDto.builder().id(comment.getId())
                .created(comment.getCreated())
                .authorName(comment.getAuthor().getName())
                .text(comment.getText())
                .build();
    }

    public static Comment fromAnswerDto(CommentAnswerDto comment) {
        return Comment.builder().id(comment.getId())
                .created(comment.getCreated())
                .author(User.builder().name(comment.getAuthorName()).build())
                .text(comment.getText())
                .build();
    }
}
