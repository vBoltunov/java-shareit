package ru.practicum.shareit.exceptions;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {
    private final String error;
    private final String message;
}