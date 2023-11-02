package ru.practicum.shareit.exception;

public class AccessDenideException extends RuntimeException {
    public AccessDenideException(String text) {
        super(text);
    }

}
