package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingCreateRequest;
import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.item.dto.ItemResponse;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserResponse;

import static org.hamcrest.Matchers.hasSize;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = BookingController.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class BookingControllerTest {

    private final MockMvc mvc;
    private final ObjectMapper objectMapper;
    @MockBean
    BookingService bookingService;
    @MockBean
    BookingMapper bookingMapper;

    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private static final Long USER_ID = 1L;
    private static final Long BOOKING_ID = 1L;

    private final User booker = User.builder().id(1L).name("Ivan").email("ivan@mail.ru").build();
    private final User owner = User.builder().id(2L).name("Ann").email("ann@mail.ru").build();

    private final Item item = Item.builder().id(1L).name("ItemName").description("ItemDescription")
            .owner(owner).build();

    private final BookingCreateRequest createRequest = BookingCreateRequest.builder()
            .itemId(1L)
            .start(LocalDateTime.now().plusHours(1))
            .end(LocalDateTime.now().plusDays(2))
            .build();

    private final Booking booking = Booking.builder()
            .id(1L)
            .start(createRequest.getStart())
            .end(createRequest.getEnd())
            .item(item)
            .booker(booker)
            .status(Status.WAITING)
            .build();

    private final BookingResponse bookingResponse = BookingResponse.builder()
            .id(1L)
            .start(booking.getStart())
            .end(booking.getEnd())
            .item(ItemResponse.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .description(item.getDescription())
                    .available(true)
                    .ownerName(owner.getName())
                    .build())
            .booker(UserResponse.builder()
                    .id(booker.getId())
                    .name(booker.getName())
                    .email(booker.getEmail())
                    .build())
            .status(Status.WAITING)
            .build();

    @Test
    @SneakyThrows
    void createBookingTest() {
        when(bookingMapper.toBooking(any(BookingCreateRequest.class), eq(USER_ID))).thenReturn(booking);
        when(bookingService.create(eq(USER_ID), any(Booking.class))).thenReturn(new Booking());
        when(bookingMapper.toResponse(any(Booking.class))).thenReturn(bookingResponse);

        mvc.perform(post("/bookings")
                        .header(USER_ID_HEADER, USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(BOOKING_ID))
                .andExpect(jsonPath("$.status").value("WAITING"));
    }

    @Test
    @SneakyThrows
    void updateStatusBookingTest() {
        Status status = Status.APPROVED;
        booking.setStatus(status);
        BookingResponse updatedResponse = BookingResponse.builder()
                .id(bookingResponse.getId())
                .start(bookingResponse.getStart())
                .end(bookingResponse.getEnd())
                .item(bookingResponse.getItem())
                .booker(bookingResponse.getBooker())
                .status(status)
                .build();

        when(bookingService.approve(eq(BOOKING_ID), eq(USER_ID), eq(true)))
                .thenReturn(booking);
        when(bookingMapper.toResponse(booking)).thenReturn(updatedResponse);

        mvc.perform(patch("/bookings/{bookingId}", BOOKING_ID)
                        .header(USER_ID_HEADER, USER_ID)
                        .param("approved", "true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(BOOKING_ID))
                .andExpect(jsonPath("$.status").value("APPROVED"));

        verify(bookingService).approve(BOOKING_ID, USER_ID, true);
    }

    @Test
    @SneakyThrows
    void getBookingByIdTest() {
        when(bookingService.getById(eq(BOOKING_ID), eq(USER_ID))).thenReturn(booking);
        when(bookingMapper.toResponse(any(Booking.class))).thenReturn(bookingResponse);

        mvc.perform(get("/bookings/{bookingId}", BOOKING_ID)
                        .header(USER_ID_HEADER, USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(BOOKING_ID));
    }

    @Test
    @SneakyThrows
    void getAllBookingsTest() {
        Booking booking2 = Booking.builder()
                .id(2L)
                .start(LocalDateTime.now().plusHours(2))
                .end(LocalDateTime.now().plusHours(3))
                .booker(booker)
                .item(Item.builder()
                        .id(2L)
                        .name("Name2")
                        .description("description2")
                        .available(true)
                        .owner(User.builder().id(3L).name("Egor").email("egor@mail.ru").build())
                        .build())
                .status(Status.APPROVED)
                .build();
        List<Booking> bookings = List.of(booking, booking2);

        BookingResponse bookingResponse2 = BookingResponse.builder()
                .id(2L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusHours(2))
                .booker(UserResponse.builder().id(2L).name("Ivan").email("ivan@mail.ru").build())
                .item(ItemResponse.builder()
                        .id(2L)
                        .name("Name2")
                        .description("description2")
                        .available(true)
                        .ownerName("Egor")
                        .build())
                .status(Status.APPROVED)
                .build();

        when(bookingService.getAll(eq(USER_ID), any(State.class))).thenReturn(bookings);
        when(bookingMapper.toResponse(booking)).thenReturn(bookingResponse);
        when(bookingMapper.toResponse(booking2)).thenReturn(bookingResponse2);

        mvc.perform(get("/bookings")
                        .header(USER_ID_HEADER, USER_ID)
                        .param("state", "ALL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[0].status").value("WAITING"))
                .andExpect(jsonPath("$[1].status").value("APPROVED"));
    }

    @Test
    @SneakyThrows
    void getAllBookingsByOwnerTest() {
        List<Booking> bookings = List.of(booking);

        when(bookingService.getAllBookingsForOwner(owner.getId(), State.ALL)).thenReturn(bookings);
        when(bookingMapper.toResponse(booking)).thenReturn(bookingResponse);

        mvc.perform(get("/bookings/owner")
                        .header(USER_ID_HEADER, owner.getId())
                        .param("state", "ALL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(bookingResponse.getId()))
                .andExpect(jsonPath("$[0].status").value(bookingResponse.getStatus().name()))
                .andExpect(jsonPath("$[0].item.id").value(bookingResponse.getItem().getId()));

        verify(bookingService, times(1)).getAllBookingsForOwner(owner.getId(), State.ALL);
        verify(bookingMapper, times(1)).toResponse(booking);
    }
}