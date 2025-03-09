package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.util.HeaderConstants;
import ru.practicum.shareit.util.PathConstants;

import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = PathConstants.REQUESTS_PATH)
public class ItemRequestController {
    private final ItemRequestService requestService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<ItemRequestDto> getRequests(@RequestHeader(HeaderConstants.USER_ID_HEADER) long userId) {
        log.info("Received GET request for all item requests for user with id: {}", userId);
        return requestService.findByUserId(userId);
    }

    @GetMapping(PathConstants.REQUEST_ID_PATH)
    @ResponseStatus(HttpStatus.OK)
    public ItemRequestDto getRequest(@PathVariable("request-id") long requestId) {
        log.info("Received GET request for request with id: {}", requestId);
        return requestService.getRequestById(requestId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemRequestDto addRequest(@RequestHeader(HeaderConstants.USER_ID_HEADER) long userId,
                                     @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Received POST request for item request for user with id: {}", userId);
        return requestService.addRequest(itemRequestDto, userId);
    }

    @PatchMapping(PathConstants.REQUEST_ID_PATH)
    @ResponseStatus(HttpStatus.OK)
    public ItemRequestDto updateRequest(@RequestHeader(HeaderConstants.USER_ID_HEADER) long userId,
                                        @PathVariable("request-id") long requestId,
                                        @RequestBody ItemRequestDto requestDto) {
        log.info("Received PATCH request for item request with id: {} for user with id: {}", requestId, userId);
        return requestService.updateRequest(requestDto, userId, requestId);
    }

    @DeleteMapping(PathConstants.REQUEST_ID_PATH)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRequest(@RequestHeader(HeaderConstants.USER_ID_HEADER) long userId,
                              @PathVariable("request-id") long requestId) {
        log.info("Received DELETE request for item request with id: {} for user with id: {}", requestId, userId);
        requestService.deleteRequest(userId, requestId);
    }

    @GetMapping(PathConstants.REQUESTS_ALL_PATH)
    @ResponseStatus(HttpStatus.OK)
    public Collection<ItemRequestDto> getAllRequests(@RequestHeader(HeaderConstants.USER_ID_HEADER) long userId) {
        log.info("Received GET request for all item requests from other users for user with id: {}", userId);
        return requestService.getAllRequestsExceptUser(userId);
    }
}