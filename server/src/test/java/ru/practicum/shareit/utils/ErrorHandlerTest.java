package ru.practicum.shareit.utils;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.ErrorHandler;
import ru.practicum.shareit.exception.ErrorResponse;
import ru.practicum.shareit.exception.InvalidArgumentException;
import ru.practicum.shareit.exception.NotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ErrorHandlerTest {

    @Test
    void invalidArgumentExceptionTest() {
        InvalidArgumentException invalidArgumentException = new InvalidArgumentException("testMessage");
        ErrorResponse errorResponse = ErrorHandler.handleInvalidArgumentException(invalidArgumentException);

        assertEquals(errorResponse.getError(), "testMessage");
    }

    @Test
    void handleConflictExceptionTest() {
        ConflictException conflictException = new ConflictException("testMessage");
        ErrorResponse errorResponse = ErrorHandler.handleConflictException(conflictException);

        assertEquals(errorResponse.getError(), "testMessage");
    }

    @Test
    void handleNotFoundExceptionTest() {
        NotFoundException notFoundException = new NotFoundException("testMessage");
        ErrorResponse errorResponse = ErrorHandler.handleNotFoundException(notFoundException);

        assertEquals(errorResponse.getError(), "testMessage");
    }
}
