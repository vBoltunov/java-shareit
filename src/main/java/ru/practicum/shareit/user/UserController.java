package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.user.dto.UserDto;
import static ru.practicum.shareit.util.PathConstants.USER_ID_PATH;

import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserServiceImpl userService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<UserDto> getUsers() {
        log.info("Fetching all users.");
        return userService.getUsers();
    }

    @GetMapping(USER_ID_PATH)
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUser(@PathVariable("user-id") Long userId) {
        log.info("Fetching user by id: {}.", userId);
        return userService.getUserById(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@Valid @RequestBody UserDto userDTO) {
        log.info("Creating user: name = {}, email = {}", userDTO.getName(), userDTO.getEmail());
        return userService.createUser(userDTO);
    }

    @PatchMapping(USER_ID_PATH)
    @ResponseStatus(HttpStatus.OK)
    public UserDto updateUser(@PathVariable("user-id") Long userId, @Valid @RequestBody UserDto userDTO) {
        log.info("Updating user with id: {}", userId);
        return userService.updateUser(userId, userDTO);
    }

    @DeleteMapping(USER_ID_PATH)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable("user-id") Long userId) {
        log.info("Deleting user with id: {}", userId);
        userService.deleteUser(userId);
    }
}