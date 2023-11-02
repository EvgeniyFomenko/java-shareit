package ru.practicum.shareit.exception.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ExceptionEntity {
    String error;
    String message;
}
