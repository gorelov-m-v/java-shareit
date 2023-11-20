package ru.practicum.shareit.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class ArgumentErrorResponse {
    private int code;
    private List<String> errors;

    public ArgumentErrorResponse(int code, List<String> errors) {
        this.code = code;
        this.errors = errors;
    }
}
