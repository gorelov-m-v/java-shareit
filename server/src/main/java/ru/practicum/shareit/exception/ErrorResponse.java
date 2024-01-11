package ru.practicum.shareit.exception;

public class ErrorResponse {
    private final String error;

    public ErrorResponse(String error, String stack) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
