package ru.practicum.shareit.item.exception;

public class EmptyNameException extends RuntimeException {
    public EmptyNameException(String string) {
        super(string);
    }
}
