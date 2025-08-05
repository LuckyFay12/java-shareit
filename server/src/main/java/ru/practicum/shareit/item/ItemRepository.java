package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findAllByOwnerOrderById(User owner);

    List<Item> findAllByItemRequest(ItemRequest itemRequest);

    @Query("SELECT i FROM Item i " +
           "WHERE i.available = true " +
           "AND (LOWER(i.name) LIKE LOWER(CONCAT('%', :text, '%')) " +
           "OR LOWER(i.description) LIKE LOWER(CONCAT('%', :text, '%')))")
    List<Item> search(@Param("text") String text);
}
