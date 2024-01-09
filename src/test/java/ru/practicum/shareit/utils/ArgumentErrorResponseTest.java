package ru.practicum.shareit.utils;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.exception.ArgumentErrorResponse;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ArgumentErrorResponseTest {
    ArgumentErrorResponse argumentErrorResponse = new ArgumentErrorResponse(400, List.of("testError"));

    @Test
    void argumentErrorResponseTest() {
        assertEquals(argumentErrorResponse.getCode(), 400);
        assertEquals(argumentErrorResponse.getErrors().get(0), "testError");
    }
}
