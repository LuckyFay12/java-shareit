package ru.practicum.shareit.item;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.item.dto.ItemResponse;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final ItemMapper itemMapper;

    @Override
    public Item create(Long userId, Item item) {
        User user = userService.getById(userId);
        item.setOwner(user);
        return itemRepository.save(item);
    }

    @Override
    public Item update(Long userId, Long itemId, Item item) {
        userService.getById(userId);
        Item updatedItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Вещь не найдена"));
        if (!updatedItem.getOwner().getId().equals(userId)) {
            throw new AccessDeniedException("Только владелец может редактировать вещь");
        }
        if (item.getName() != null) {
            updatedItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            updatedItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            updatedItem.setAvailable(item.getAvailable());
        }
        return itemRepository.save(updatedItem);
    }

    @Override
    public ItemResponse getById(Long userId, Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Вещь с id " + itemId + " не найдена"));
        ItemResponse response = itemMapper.toItemResponse(item);
        if (item.getOwner().getId().equals(userId)) {
            LocalDateTime now = LocalDateTime.now();

            response.setNextBooking(
                    bookingRepository.findFirstByItemAndStartAfterAndStatusInOrderByStartAsc(item, now,
                                    List.of(Status.APPROVED, Status.WAITING))
                            .map(bookingMapper::toShortInfoDto)
                            .orElse(null)
            );

            response.setLastBooking(
                    bookingRepository.findFirstByItemAndEndBeforeAndStatusInOrderByEndDesc(item, now,
                                    List.of(Status.APPROVED))
                            .map(bookingMapper::toShortInfoDto)
                            .orElse(null)
            );
        }
        return response;
    }

    @Override
    public List<Item> getUserItems(Long userId) {
        User owner = userService.getById(userId);
        return itemRepository.findAllByOwnerOrderById(owner);
    }

    @Override
    public List<Item> search(String text) {
        if (text.isEmpty() || text.isBlank()) {
            log.info("Пустой поисковый запрос - возвращаем empty list");
            return Collections.emptyList();
        }
        return itemRepository.search(text);
    }

    @Override
    public Comment addComment(Comment comment) {
        Item item = itemRepository.findById(comment.getItem().getId())
                .orElseThrow(() -> new ItemNotFoundException("Вещь не найдена"));
        User booker = userService.getById(comment.getAuthor().getId());

        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings = bookingRepository.findAllByItemAndBooker(item, booker);
        if (bookings.stream().noneMatch(b -> b.isFinished(now))) {
            throw new ValidationException("Отзыв может оставлять только пользователь, который брал вещь в аренду");
        }
        comment.setCreated(now);// Установка даты
        comment.setItem(item);
        comment.setAuthor(booker);
        return commentRepository.save(comment);
    }
}
