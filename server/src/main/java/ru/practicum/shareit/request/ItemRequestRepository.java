package ru.practicum.shareit.request;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    Collection<ItemRequest> findByUserId(Long userId);

    @Transactional
    void deleteByUserIdAndRequestId(Long userId, Long requestId);
}
