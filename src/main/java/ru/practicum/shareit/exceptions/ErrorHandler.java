package ru.practicum.shareit.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(final NotFoundException e) {
        Map<String, String> messages = new HashMap<>();
        messages.put("error", e.getMessage());
        return new ErrorResponse("error:", messages);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleInternalErrors(final Throwable e) {
        Map<String, String> messages = new HashMap<>();
        messages.put("error", "Произошла непредвиденная ошибка: " + e.getMessage());
        return new ErrorResponse("error:", messages);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleForbidden(final ForbiddenException e) {
        Map<String, String> messages = new HashMap<>();
        messages.put("error", e.getMessage());
        return new ErrorResponse("error:", messages);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, IllegalArgumentException.class})
    public ResponseEntity<ErrorResponse> handleValidationExceptions(Exception e) {
        Map<String, String> errors = new HashMap<>();
        String errorMessage;

        if (e instanceof MethodArgumentNotValidException ex) {
            ex.getBindingResult().getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage())
            );
            errorMessage = "Validation Error";
            ErrorResponse response = ErrorResponse.builder()
                    .error(errorMessage)
                    .messages(errors)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } else if (e instanceof IllegalArgumentException) {
            errorMessage = e.getMessage();
            if ("Email must be provided".equals(errorMessage) || "Invalid email format".equals(errorMessage)) {
                errors.put("error", errorMessage);
                ErrorResponse response = ErrorResponse.builder()
                        .error("Validation Error")
                        .messages(errors)
                        .build();
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            } else {
                errors.put("error", errorMessage);
                ErrorResponse response = ErrorResponse.builder()
                        .error("Conflict Error")
                        .messages(errors)
                        .build();
                return new ResponseEntity<>(response, HttpStatus.CONFLICT);
            }
        }

        errorMessage = "Internal Server Error";
        errors.put("error", errorMessage);
        ErrorResponse response = ErrorResponse.builder()
                .error(errorMessage)
                .messages(errors)
                .build();
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}