package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponse;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRequestControllerTest {

    private final MockMvc mvc;
    private final ObjectMapper objectMapper;
    @MockBean
    ItemRequestService itemRequestService;
    @MockBean
    ItemRequestMapper itemRequestMapper;

    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private static final Long USER_ID = 1L;
    private static final Long REQUEST_ID = 1L;

    private final ItemRequestDto requestDto = ItemRequestDto.builder().description("Need item").build();

    private final ItemRequest itemRequest = ItemRequest.builder()
            .id(REQUEST_ID)
            .description("Need item")
            .requestor(User.builder().id(USER_ID).name("Requestor").email("Requestor@mail.ru").build())
            .created(LocalDateTime.now())
            .build();

    private final ItemRequestResponse response = ItemRequestResponse.builder()
            .id(REQUEST_ID)
            .description(itemRequest.getDescription())
            .requestorId(itemRequest.getRequestor().getId())
            .created(itemRequest.getCreated())
            .items(List.of())
            .build();

    @Test
    @SneakyThrows
    void createRequest_ShouldCreateRequest() {
        when(itemRequestMapper.toItemRequest(eq(USER_ID), any(ItemRequestDto.class)))
                .thenReturn(itemRequest);
        when(itemRequestService.create(eq(USER_ID), any(ItemRequest.class)))
                .thenReturn(itemRequest);
        when(itemRequestMapper.toItemRequestResponse(itemRequest))
                .thenReturn(response);

        mvc.perform(post("/requests")
                        .header(USER_ID_HEADER, USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(REQUEST_ID))
                .andExpect(jsonPath("$.description").value("Need item"));

        verify(itemRequestService).create(USER_ID, itemRequest);
    }

    @Test
    @SneakyThrows
    void getUserRequests_ShouldReturnRequests() {
        when(itemRequestService.getUserRequests(USER_ID))
                .thenReturn(List.of(itemRequest));
        when(itemRequestMapper.toItemRequestResponse(any(ItemRequest.class)))
                .thenReturn(response);

        mvc.perform(get("/requests")
                        .header(USER_ID_HEADER, USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(REQUEST_ID));
    }

    @Test
    @SneakyThrows
    void getAllRequests_ShouldReturnAllRequests() {
        when(itemRequestService.getAllRequests(USER_ID))
                .thenReturn(List.of(itemRequest));
        when(itemRequestMapper.toItemRequestResponse(any(ItemRequest.class)))
                .thenReturn(response);

        mvc.perform(get("/requests/all")
                        .header(USER_ID_HEADER, USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(REQUEST_ID));
    }

    @Test
    @SneakyThrows
    void getRequestById_ShouldReturnRequest() {
        when(itemRequestService.getRequestById(USER_ID, REQUEST_ID))
                .thenReturn(response);

        mvc.perform(get("/requests/{requestId}", REQUEST_ID)
                        .header(USER_ID_HEADER, USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(REQUEST_ID));
    }

    @Test
    @SneakyThrows
    void getRequestById_ShouldReturn404WhenNotFound() {
        when(itemRequestService.getRequestById(USER_ID, REQUEST_ID))
                .thenThrow(new ItemRequestNotFoundException("Request not found"));

        mvc.perform(get("/requests/{requestId}", REQUEST_ID)
                        .header(USER_ID_HEADER, USER_ID))
                .andExpect(status().isNotFound());
    }
}