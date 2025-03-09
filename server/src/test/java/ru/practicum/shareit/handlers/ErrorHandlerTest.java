package ru.practicum.shareit.handlers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.exceptions.ForbiddenException;
import ru.practicum.shareit.exceptions.NotFoundException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {ErrorHandler.class, ErrorHandlerTest.TestController.class})
@Import(ErrorHandlerTest.TestController.class)
class ErrorHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @RestController
    static class TestController {
        @GetMapping("/test/not-found")
        public void throwNotFound() {
            throw new NotFoundException("Test Not Found");
        }

        @GetMapping("/test/forbidden")
        public void throwForbidden() {
            throw new ForbiddenException("Test Forbidden");
        }

        @GetMapping("/test/internal-error")
        public void throwInternalError() {
            throw new RuntimeException("Test Internal Error");
        }

        @GetMapping("/test/illegal-argument")
        public void throwIllegalArgument() {
            throw new IllegalArgumentException("Test Illegal Argument");
        }

        @GetMapping("/test/email-error")
        public void throwEmailError() {
            throw new IllegalArgumentException("Email must be provided");
        }

        @PostMapping("/test/validation-error")
        public void throwValidationError(@Valid @RequestBody TestDto dto) {
            // Метод не будет выполнен, так как валидация выбросит исключение
        }
    }

    @Setter
    @Getter
    static class TestDto {
        @NotBlank(message = "Field must not be blank")
        private String field;
    }

    @Test
    void handleNotFoundException_ShouldReturn404() throws Exception {
        mockMvc.perform(get("/test/not-found")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("error:"))
                .andExpect(jsonPath("$.messages.error").value("Test Not Found"));
    }

    @Test
    void handleForbiddenException_ShouldReturn403() throws Exception {
        mockMvc.perform(get("/test/forbidden")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value("error:"))
                .andExpect(jsonPath("$.messages.error").value("Test Forbidden"));
    }

    @Test
    void handleInternalError_ShouldReturn500() throws Exception {
        mockMvc.perform(get("/test/internal-error")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("error:"))
                .andExpect(jsonPath("$.messages.error").value("Произошла непредвиденная ошибка: Test Internal Error"));
    }

    @Test
    void handleIllegalArgumentException_ShouldReturn409() throws Exception {
        mockMvc.perform(get("/test/illegal-argument")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Conflict Error"))
                .andExpect(jsonPath("$.messages.error").value("Test Illegal Argument"));
    }

    @Test
    void handleEmailError_ShouldReturn400() throws Exception {
        mockMvc.perform(get("/test/email-error")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Error"))
                .andExpect(jsonPath("$.messages.error").value("Email must be provided"));
    }

    @Test
    void handleValidationException_ShouldReturn400() throws Exception {
        mockMvc.perform(post("/test/validation-error")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"field\": \"\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Error"))
                .andExpect(jsonPath("$.messages.field").value("Field must not be blank"));
    }
}