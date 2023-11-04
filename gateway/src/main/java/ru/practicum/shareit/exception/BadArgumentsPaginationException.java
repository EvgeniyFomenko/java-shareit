package ru.practicum.shareit.exception;

public class BadArgumentsPaginationException extends RuntimeException {
    public BadArgumentsPaginationException(String message) {
        super(message);
    }
}
