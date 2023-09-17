package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.item.model.ExceptionEntity;
import ru.practicum.shareit.user.exception.AlreadyEmailExistException;

@RestControllerAdvice
public class ControllerException {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionEntity notFoundExceptionHandler(UserNotFoundException userNotFoundException) {
        return new ExceptionEntity("Not found such user", userNotFoundException.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionEntity emptyNameExceptionHandler(EmptyNameException emptyNameException) {
        return new ExceptionEntity("Empty name exception", emptyNameException.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionEntity accessDenideExceptionHandler(AccessDenideException accessDenideException) {
        return new ExceptionEntity("User don`t have permission to change this item", accessDenideException.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionEntity alreadyEmailExistExistHandler(AlreadyEmailExistException alreadyEmailExistException) {
        return new ExceptionEntity("Such email already busy", alreadyEmailExistException.getMessage());
    }
}
