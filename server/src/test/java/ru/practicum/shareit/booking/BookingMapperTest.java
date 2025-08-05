package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingCreateRequest;
import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.booking.dto.BookingShortInfoDto;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemResponse;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserResponse;

import java.time.LocalDateTime;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingMapperTest {

    @InjectMocks
    private BookingMapperImpl bookingMapper;

    @Mock
    private UserMapper userMapper;

    @Mock
    private ItemMapper itemMapper;

    private final LocalDateTime now = LocalDateTime.now();
    private final User booker = User.builder().id(1L).name("Booker").email("booker@mail.ru").build();
    private final User owner = User.builder().id(2L).name("Owner").email("owner@mail.ru").build();
    private final Item item = Item.builder()
            .id(1L)
            .name("Item")
            .description("Description")
            .available(true)
            .owner(owner)
            .build();

    @Test
    void toBooking_ShouldMapCorrectly() {
        BookingCreateRequest request = BookingCreateRequest.builder()
                .itemId(1L)
                .start(now.plusDays(1))
                .end(now.plusDays(2))
                .build();
        Long userId = 1L;

        Booking result = bookingMapper.toBooking(request, userId);

        assertNotNull(result);
        assertNull(result.getId());
        assertEquals(userId, result.getBooker().getId());
        assertEquals(request.getItemId(), result.getItem().getId());
        assertEquals(request.getStart(), result.getStart());
        assertEquals(request.getEnd(), result.getEnd());
        assertEquals(Status.WAITING, result.getStatus());
    }

    @Test
    void toResponse_ShouldMapCorrectly() {
        Booking booking = Booking.builder()
                .id(1L)
                .start(now.plusDays(1))
                .end(now.plusDays(2))
                .item(item)
                .booker(booker)
                .status(Status.APPROVED)
                .build();

        UserResponse userResponse = UserResponse.builder()
                .id(booker.getId())
                .name(booker.getName())
                .email(booker.getEmail())
                .build();

        ItemResponse itemResponse = ItemResponse.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .ownerName(owner.getName())
                .build();

        when(userMapper.toUserResponse(booker)).thenReturn(userResponse);
        when(itemMapper.toItemResponse(item)).thenReturn(itemResponse);

        BookingResponse result = bookingMapper.toResponse(booking);

        assertNotNull(result);
        assertEquals(booking.getId(), result.getId());
        assertEquals(booking.getStart(), result.getStart());
        assertEquals(booking.getEnd(), result.getEnd());
        assertEquals(booking.getStatus(), result.getStatus());

        assertEquals(userResponse.getId(), result.getBooker().getId());
        assertEquals(itemResponse.getId(), result.getItem().getId());
    }

    @Test
    void toShortInfoDto_ShouldMapCorrectly() {
        Booking booking = Booking.builder()
                .id(1L)
                .start(now.plusDays(1))
                .end(now.plusDays(2))
                .booker(booker)
                .status(Status.APPROVED)
                .build();

        BookingShortInfoDto result = bookingMapper.toShortInfoDto(booking);

        assertNotNull(result);
        assertEquals(booking.getId(), result.getId());
        assertEquals(booking.getStart(), result.getStart());
        assertEquals(booking.getEnd(), result.getEnd());
    }
}