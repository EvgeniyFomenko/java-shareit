package ru.practicum.shareit.item.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.item.model.ExceptionEntity;

@RestControllerAdvice
public class ControllerException {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionEntity notFoundHandler(NotFoundException notFoundException) {
        return new ExceptionEntity("Not Found Exception",notFoundException.getMessage());

    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionEntity nullPointerExceptionHandler(NullPointerException nullPointerException) {
        return new ExceptionEntity("Null pointer Exception", nullPointerException.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionEntity nullPointerExceptionHandler(EmptyNameException emptyNameException) {
        return new ExceptionEntity("Empty name exception", emptyNameException.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionEntity nullPointerExceptionHandler(AccessDenideException accessDenideException) {
        return new ExceptionEntity("User don`t have permission to change this item", accessDenideException.getMessage());
    }
}
