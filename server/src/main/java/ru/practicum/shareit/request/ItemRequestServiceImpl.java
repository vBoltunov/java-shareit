package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final UserRepository userRepository;
    private final ItemRequestRepository requestRepository;

    @Override
    public Collection<ItemRequestDto> findByUserId(long userId) {
        return requestRepository.findByUserId(userId).stream()
                .map(ItemRequestMapper::convertToDto)
                .toList();
    }

    @Override
    public ItemRequestDto getRequestById(long requestId) {
        ItemRequest request = fetchRequestById(requestId);

        return ItemRequestMapper.convertToDto(request);
    }

    @Override
    public ItemRequestDto addRequest(ItemRequestDto requestDto, long userId) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User with id {} not found", userId);
                    return new NotFoundException(String.format("User with id %s not found", userId));
                });

        ItemRequest request = ItemRequestMapper.convertToEntity(requestDto, owner);

        request.setUserId(userId);
        request.setDescription(requestDto.getDescription());

        ItemRequest savedRequest = requestRepository.save(request);

        return ItemRequestMapper.convertToDto(savedRequest);
    }

    @Override
    public ItemRequestDto updateRequest(ItemRequestDto requestDto, long userId, long requestId) {
        log.info("Checking user exists with id: {}", userId);
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format("User with id %s not found", userId));
        }

        log.info("Starting update request with id: {}", requestId);
        ItemRequest request = requestRepository.findByUserId(userId).stream()
                .filter(i -> i.getRequestId() == requestId)
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Request with id %s not found", requestId)));

        log.info("Updating request fields");
        ItemRequestMapper.updateRequestFields(request, requestDto);

        ItemRequest updatedRequest = requestRepository.save(request);

        log.info("Request updated successfully: id = {}", updatedRequest.getRequestId());

        return ItemRequestMapper.convertToDto(updatedRequest);
    }

    @Override
    public void deleteRequest(long userId, long requestId) {
        requestRepository.deleteByUserIdAndRequestId(userId, requestId);
        log.info("Request deleted successfully: id = {}", requestId);
    }

    @Override
    public Collection<ItemRequestDto> getAllRequestsExceptUser(long userId) {
        log.info("Fetching all requests except those created by user with id: {}", userId);
        return requestRepository.findAll().stream()
                .filter(request -> request.getUserId() != userId)
                .map(ItemRequestMapper::convertToDto)
                .toList();
    }

    private ItemRequest fetchRequestById(Long requestId) {
        if (requestId == null) {
            log.error("Request id must not be null.");
            throw new IllegalArgumentException("Request id must not be null.");
        }

        ItemRequest fetchedRequest = requestRepository.findById(requestId)
                .orElseThrow(() -> {
                    log.error("Request with id {} not found", requestId);
                    return new NotFoundException(String.format("Request with id %s not found", requestId));
                });

        log.info("Item fetched successfully: id = {}, description = {}",
                fetchedRequest.getRequestId(), fetchedRequest.getDescription());

        return fetchedRequest;
    }
}
