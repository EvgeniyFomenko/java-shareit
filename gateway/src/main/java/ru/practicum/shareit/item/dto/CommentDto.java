package ru.practicum.shareit.item.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    @NonNull
    private long id;
    @NonNull
    private long itemId;
    @NotBlank
    private String text;
}
