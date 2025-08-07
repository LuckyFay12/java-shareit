package ru.practicum.shareit.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    List<ItemRequest> findByRequestor_IdOrderByCreatedDesc(Long requestorId);

    @Query("SELECT i FROM ItemRequest i " +
           "WHERE i.requestor.id != :userId " +
           "ORDER BY i.created DESC")
    List<ItemRequest> findAllOtherUsersRequests(@Param("userId") long userId);

}
