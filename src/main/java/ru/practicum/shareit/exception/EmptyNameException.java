package ru.practicum.shareit.exception;

public class EmptyNameException extends RuntimeException {
    public EmptyNameException(String string) {
        super(string);
    }
}
