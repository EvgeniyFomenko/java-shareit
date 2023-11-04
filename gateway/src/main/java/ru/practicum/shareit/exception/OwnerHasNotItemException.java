package ru.practicum.shareit.exception;

public class OwnerHasNotItemException extends RuntimeException {
    public OwnerHasNotItemException(String message) {
        super(message);
    }
}
