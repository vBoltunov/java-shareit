package ru.practicum.shareit.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exceptions.ErrorResponse;
import ru.practicum.shareit.exceptions.ForbiddenException;
import ru.practicum.shareit.exceptions.NotFoundException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(final NotFoundException e) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage(), null).getBody();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleInternalErrors(final Throwable e) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                "Произошла непредвиденная ошибка: " + e.getMessage(), null).getBody();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleForbidden(final ForbiddenException e) {
        return buildErrorResponse(HttpStatus.FORBIDDEN, e.getMessage(), null).getBody();
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, IllegalArgumentException.class})
    public ResponseEntity<ErrorResponse> handleValidationExceptions(Exception e) {
        HttpStatus status;
        String errorMessage;
        Map<String, String> errors = new HashMap<>();

        if (e instanceof MethodArgumentNotValidException ex) {
            ex.getBindingResult().getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage())
            );
            errorMessage = "Validation Error";
            status = HttpStatus.BAD_REQUEST;
        } else if (e instanceof IllegalArgumentException) {
            errorMessage = e.getMessage();
            status = ("Email must be provided".equals(errorMessage) ||
                    "Invalid email format".equals(errorMessage))
                    ? HttpStatus.BAD_REQUEST
                    : HttpStatus.CONFLICT;
            errors.put("error", errorMessage);
        } else {
            errorMessage = "Internal Server Error";
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            errors.put("error", errorMessage);
        }

        return buildErrorResponse(status, errorMessage, errors);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status,
                                                             String errorMessage,
                                                             Map<String, String> errors) {
        if (errors == null) {
            errors = new HashMap<>();
            errors.put("error", errorMessage);
        }
        ErrorResponse response = ErrorResponse.builder()
                .error(errorMessage)
                .messages(errors)
                .build();
        return new ResponseEntity<>(response, status);
    }
}