package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

@Slf4j
@Service
public class UserClient extends BaseClient {
    private static final String API_PREFIX = "/users";

    @Autowired
    public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> getUsers() {
        log.debug("Sending GET request for all users");
        return get("");
    }

    public ResponseEntity<Object> getUserById(long userId) {
        log.debug("Sending GET request for user with id: {}", userId);
        return get("/" + userId);
    }

    public ResponseEntity<Object> createUser(UserDto userDto) {
        log.debug("Sending POST request to create user: {}", userDto);
        return post("", userDto);
    }

    public ResponseEntity<Object> updateUser(long userId, UserUpdateDto userDto) {
        log.debug("Sending PATCH request to update user with id: {}", userId);
        return patch("/" + userId, userDto);
    }

    public ResponseEntity<Object> deleteUser(long userId) {
        log.debug("Sending DELETE request for user with id: {}", userId);
        return delete("/" + userId);
    }
}
