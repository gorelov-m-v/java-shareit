package ru.practicum.shareit.utils;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.exception.ErrorResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ErrorResponseTest {
    ErrorResponse errorResponse = new ErrorResponse("NotFoundException", "stack");


    @Test
    void errorResponseTest() {
        assertEquals(errorResponse.getError(), "NotFoundException");
    }

}
