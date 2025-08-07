package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.BookingNotFoundException;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemRepository itemRepository;

    @Override
    public Booking create(Long userId, Booking booking) {
        User user = userService.getById(userId);
        Item item = itemRepository.findById(booking.getItem().getId())
                .orElseThrow(() -> new ItemNotFoundException("Вещь не найдена"));
        if (!item.getAvailable()) {
            throw new ValidationException("Вещь недоступна для бронирования");
        }
        if (item.getOwner().getId().equals(booking.getBooker().getId())) {
            throw new ValidationException("Владелец не может бронировать свою вещь");
        }
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(Status.WAITING);
        return bookingRepository.save(booking);
    }

    @Override
    public Booking approve(Long bookingId, Long userId, boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Бронь не найдена"));
        if (!Objects.equals(userId, booking.getItem().getOwner().getId())) {
            throw new AccessDeniedException("Подтверждение или отклонение запроса на бронирование может быть выполнено только владельцем вещи");
        }
        if (!Objects.equals(Status.WAITING, booking.getStatus())) {
            throw new ValidationException("Статус бронирования должен быть WAITING");
        }
        booking.setStatus(approved ? Status.APPROVED : Status.REJECTED);
        return bookingRepository.save(booking);
    }

    @Override
    public Booking getById(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Бронь не найдена"));
        if (!booking.getBooker().getId().equals(userId) && !booking.getItem().getOwner().getId().equals(userId)) {
            throw new AccessDeniedException("Доступ к бронированию имеют только владелец вещи и арендатор");
        }
        return booking;
    }

    @Override
    public List<Booking> getAll(Long bookerId, State state) {
        User booker = userService.getById(bookerId);
        LocalDateTime now = LocalDateTime.now();

        switch (state) {
            case State.WAITING:
                return bookingRepository.findAllByBookerAndStatusList(booker, List.of(Status.WAITING));
            case State.REJECTED:
                return bookingRepository.findAllByBookerAndStatusList(booker,
                        List.of(Status.REJECTED, Status.CANCELED));
            case State.CURRENT:
                return bookingRepository.findCurrentByBookerItems(booker, now);
            case State.FUTURE:
                return bookingRepository.findFutureByBookerItems(booker, now);
            case State.PAST:
                return bookingRepository.findPastByBookerItems(booker, now);
            case State.ALL:
                return bookingRepository.findAllByBookerOrderByStartDesc(booker);

            default:
                return Collections.emptyList();
        }
    }

    @Override
    public List<Booking> getAllBookingsForOwner(Long ownerId, State state) {
        User owner = userService.getById(ownerId);
        List<Item> userItems = itemRepository.findAllByOwnerOrderById(owner);
        if (userItems.isEmpty()) {
            throw new ValidationException("Список бронирований могут получать пользователи, которые владеют хотя бы 1 вещью");
        }
        LocalDateTime now = LocalDateTime.now();

        switch (state) {
            case State.WAITING:
                return bookingRepository.findAllByOwnerItemsAndStatusList(owner, List.of(Status.WAITING));
            case State.REJECTED:
                return bookingRepository.findAllByOwnerItemsAndStatusList(owner,
                        List.of(Status.REJECTED, Status.CANCELED));
            case State.CURRENT:
                return bookingRepository.findCurrentByOwnerItems(owner, now);
            case State.FUTURE:
                return bookingRepository.findFutureByOwnerItems(owner, now);
            case State.PAST:
                return bookingRepository.findPastByOwnerItems(owner, now);
            case State.ALL:
                return bookingRepository.findAllByItemOwnerOrderByStartDesc(owner);

            default:
                return Collections.emptyList();
        }
    }
}

