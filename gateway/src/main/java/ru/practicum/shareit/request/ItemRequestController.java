package ru.practicum.shareit.request;

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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.util.HeaderConstants;
import ru.practicum.shareit.util.PathConstants;

@Controller
@RequestMapping(path = PathConstants.REQUESTS_PATH)
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {
    private final ItemRequestClient requestClient;

    @GetMapping
    public ResponseEntity<Object> getRequests(@RequestHeader(HeaderConstants.USER_ID_HEADER) @Positive long userId) {
        log.info("Sending GET request for all item requests for user with id: {}", userId);
        return requestClient.findByUserId(userId);
    }

    @GetMapping(PathConstants.REQUEST_ID_PATH)
    public ResponseEntity<Object> getRequest(@PathVariable("request-id") @Positive long requestId) {
        log.info("Sending GET request for item request with id: {}", requestId);
        return requestClient.getRequestById(requestId);
    }

    @PostMapping
    public ResponseEntity<Object> addRequest(@RequestHeader(HeaderConstants.USER_ID_HEADER) @Positive long userId,
                                             @RequestBody @Valid ItemRequestDto requestDto) {
        log.info("Sending POST request for item request {} for userId {}", requestDto, userId);
        return requestClient.createRequest(userId, requestDto);
    }

    @PatchMapping(PathConstants.REQUEST_ID_PATH)
    public ResponseEntity<Object> updateRequest(@RequestHeader(HeaderConstants.USER_ID_HEADER) @Positive long userId,
                                                @PathVariable("request-id") @Positive long requestId,
                                                @RequestBody @Valid ItemRequestDto requestDto) {
        log.info("Sending PATCH request for item request with id: {} for user with id: {}", requestId, userId);
        return requestClient.updateRequest(requestId, userId, requestDto);
    }

    @DeleteMapping(PathConstants.REQUEST_ID_PATH)
    public ResponseEntity<Object> deleteRequest(@RequestHeader(HeaderConstants.USER_ID_HEADER) @Positive long userId,
                                                @PathVariable("request-id") @Positive long requestId) {
        log.info("Sending DELETE request for item request with id: {} for user with id: {}", requestId, userId);
        return requestClient.deleteRequest(requestId, userId);
    }

    @GetMapping(PathConstants.REQUESTS_ALL_PATH)
    public ResponseEntity<Object> getAllRequests(@RequestHeader(HeaderConstants.USER_ID_HEADER) @Positive long userId) {
        log.info("Sending GET request for all item requests from other users for user with id: {}", userId);
        return requestClient.getAllRequests(userId);
    }
}
