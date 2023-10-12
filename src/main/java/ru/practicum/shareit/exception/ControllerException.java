package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exception.model.ExceptionEntity;
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

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionEntity itemNotfound(final ItemNotFoundException itemNotFoundException) {
        return new ExceptionEntity("Not Found Exception", itemNotFoundException.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionEntity ownerHasNotItem(final OwnerHasNotItemException ownerHasNotItemException) {
        return new ExceptionEntity("Owner has not item exception", ownerHasNotItemException.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionEntity itemUnavailable(final ItemUnavailableException itemUnavailableException) {
        return new ExceptionEntity("Item unavailable exception", itemUnavailableException.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionEntity bookingStartTimeAfterEnd(final BookingTimeException bookingTimeException) {
        return new ExceptionEntity("Start or end time invalid ", bookingTimeException.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionEntity bookingNotFound(final BookingNotFoundException bookingNotFoundException) {
        return new ExceptionEntity("Booking unavailable exception", bookingNotFoundException.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionEntity bookingNotFound(final NotFoundStateException notFoundStateException) {
        return new ExceptionEntity("State unavailable ", notFoundStateException.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionEntity unsupportedStatus(final UnknownStatus unknownStatus) {
        return new ExceptionEntity("Unknown state: UNSUPPORTED_STATUS", unknownStatus.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionEntity changeDeprecated(final ChangeDeprecated changeDeprecated) {
        return new ExceptionEntity("Changes deprecated", changeDeprecated.getMessage());
    }
}
