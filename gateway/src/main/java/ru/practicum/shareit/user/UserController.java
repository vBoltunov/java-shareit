package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.util.PathConstants;

@Controller
@RequestMapping(path = PathConstants.USERS_PATH)
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> getUsers() {
        log.info("Sending GET request for all users");
        return userClient.getUsers();
    }

    @GetMapping(PathConstants.USER_ID_PATH)
    public ResponseEntity<Object> getUser(@PathVariable("user-id") @Positive Long userId) {
        log.info("Sending GET request for user with id: {}", userId);
        return userClient.getUserById(userId);
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody @Valid UserDto userDto) {
        log.info("Sending POST request for user {}", userDto);
        return userClient.createUser(userDto);
    }

    @PatchMapping(PathConstants.USER_ID_PATH)
    public ResponseEntity<Object> updateUser(@PathVariable("user-id") @Positive long userId,
                                             @RequestBody @Valid UserUpdateDto userDto) {
        log.info("Sending PATCH request for user with id {}", userId);
        return userClient.updateUser(userId, userDto);
    }

    @DeleteMapping(PathConstants.USER_ID_PATH)
    public ResponseEntity<Object> deleteUser(@PathVariable("user-id") @Positive Long userId) {
        log.info("Sending DELETE request for user with id: {}", userId);
        return userClient.deleteUser(userId);
    }
}
