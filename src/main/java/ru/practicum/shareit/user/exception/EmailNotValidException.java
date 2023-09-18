package ru.practicum.shareit.user.exception;


public class EmailNotValidException extends RuntimeException {

    public EmailNotValidException(String exception) {
        super(exception);
    }
}
