package ru.practicum.shareit.booking;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.dto.BookingCreateRequest;
import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.booking.dto.BookingShortInfoDto;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.user.UserMapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class, ItemMapper.class})
public interface BookingMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "booker.id", source = "userId")
    @Mapping(target = "status", constant = "WAITING")
    @Mapping(target = "item.id", source = "request.itemId")
    Booking toBooking(BookingCreateRequest request, Long userId);

    BookingResponse toResponse(Booking booking);

    BookingShortInfoDto toShortInfoDto(Booking booking);
}
