package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreateRequest;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {

    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> createBooking(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                                                @Valid @RequestBody BookingCreateRequest bookingCreateRequest) {
        return bookingClient.createBooking(userId, bookingCreateRequest);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> updateStatusBooking(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                                                      @PathVariable @Positive Long bookingId,
                                                      @RequestParam boolean approved) {
        return bookingClient.updateStatusBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                                             @PathVariable @Positive Long bookingId) {
        return bookingClient.getBooking(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getBookingsByUser(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                                                    @RequestParam(defaultValue = "ALL") State state) {
        return bookingClient.getBookingsByUser(userId, state);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllBookingsForOwner(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                                                         @RequestParam(defaultValue = "ALL") State state) {
        return bookingClient.getAllBookingsForOwner(userId, state);
    }
}