package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreateRequest;
import ru.practicum.shareit.booking.dto.BookingResponse;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final BookingMapper bookingMapper;

    @PostMapping
    public BookingResponse createBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @RequestBody BookingCreateRequest request) {
        log.info("Получен HTTP-запрос на добавление бронирования: {}", request);
        return bookingMapper.toResponse(bookingService.create(userId, bookingMapper.toBooking(request, userId)));
    }

    @PatchMapping("/{bookingId}")
    public BookingResponse updateStatusBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                               @PathVariable("bookingId") Long bookingId,
                                               @RequestParam(name = "approved", required = true) Boolean approved) {
        log.info("Получен HTTP-запрос на обновление бронирования");
        return bookingMapper.toResponse(bookingService.approve(bookingId, userId, approved));
    }

    @GetMapping("/{bookingId}")
    public BookingResponse get(@RequestHeader("X-Sharer-User-Id") Long userId,
                               @PathVariable("bookingId") Long bookingId) {
        log.info("Получен HTTP-запрос на получение бронирования по id: {} у пользователя с id: {}", bookingId, userId);
        return bookingMapper.toResponse(bookingService.getById(bookingId, userId));
    }

    @GetMapping
    public List<BookingResponse> getAll(@RequestHeader("X-Sharer-User-Id") Long userId,
                                        @RequestParam(defaultValue = "ALL") State state) {
        log.info("Получен HTTP-запрос на получение всех броней у пользователя с id: {}", userId);
        return bookingService.getAll(userId, state).stream()
                .map(bookingMapper::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/owner")
    public List<BookingResponse> getAllBookingsForOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                        @RequestParam(defaultValue = "ALL") State state) {
        log.info("Получен HTTP-запрос на получение всех броней у пользователя с id: {}", userId);
        return bookingService.getAllBookingsForOwner(userId, state).stream()
                .map(bookingMapper::toResponse)
                .collect(Collectors.toList());
    }
}
