package ru.practicum.shareit.user.exception;

public class AlreadyEmailExistException extends RuntimeException {
    public AlreadyEmailExistException(String exception) {
        super(exception);
    }
}
