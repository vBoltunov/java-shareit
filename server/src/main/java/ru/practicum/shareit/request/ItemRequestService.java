package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.Collection;

public interface ItemRequestService {
    Collection<ItemRequestDto> findByUserId(long userId);

    ItemRequestDto getRequestById(long requestId);

    ItemRequestDto addRequest(ItemRequestDto requestDto, long userId);

    ItemRequestDto updateRequest(ItemRequestDto requestDto, long userId, long itemId);

    void deleteRequest(long userId, long requestId);

    Collection<ItemRequestDto> getAllRequestsExceptUser(long userId);
}
