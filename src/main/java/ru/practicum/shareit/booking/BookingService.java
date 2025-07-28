package ru.practicum.shareit.booking;

import java.util.List;

public interface BookingService {

    Booking create(Long userId, Booking booking);

    Booking approve(Long bookingId, Long userId, boolean approved);

    Booking getById(Long bookingId, Long userId);

    List<Booking> getAll(Long userId, State state);

    List<Booking> getAllBookingsForOwner(Long ownerId, State state);
}
