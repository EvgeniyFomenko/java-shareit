package ru.practicum.shareit.exception;

public class UnknownStatus extends RuntimeException {
    public UnknownStatus(String message) {
        super(message);
    }
}
