package shareit.exceptions;

import lombok.Builder;

import java.util.Map;

@Builder
public record ErrorResponse(String error, Map<String, String> messages) {
}