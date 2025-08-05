package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemControllerTest {

    private final MockMvc mvc;
    private final ObjectMapper objectMapper;
    @MockBean
    ItemService itemService;
    @MockBean
    ItemMapper itemMapper;
    @MockBean
    CommentMapper commentMapper;

    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private static final Long USER_ID = 1L;
    private static final Long ITEM_ID = 1L;

    private final ItemCreateRequest createRequest = ItemCreateRequest.builder()
            .name("Item")
            .description("Description")
            .available(true)
            .build();

    private final ItemUpdateRequest updateRequest = ItemUpdateRequest.builder()
            .name("Updated Item")
            .description("Updated Description")
            .available(false)
            .build();

    private final ItemResponse itemResponse = ItemResponse.builder()
            .id(ITEM_ID)
            .name("Item")
            .description("Description")
            .available(true)
            .build();

    private final CommentCreateRequest commentRequest = CommentCreateRequest.builder().text("Comment").build();
    private final CommentResponse commentResponse = CommentResponse.builder()
            .id(1L)
            .text("Comment")
            .authorName("User")
            .build();

    @Test
    @SneakyThrows
    void create_ShouldCreateItem() {
        when(itemMapper.toItem(any(ItemCreateRequest.class), anyLong())).thenReturn(new Item());
        when(itemService.create(anyLong(), any(Item.class))).thenReturn(new Item());
        when(itemMapper.toItemResponse(any(Item.class))).thenReturn(itemResponse);

        mvc.perform(post("/items")
                        .header(USER_ID_HEADER, USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ITEM_ID))
                .andExpect(jsonPath("$.name").value("Item"));
    }

    @Test
    @SneakyThrows
    void update_ShouldUpdateItem() {
        when(itemMapper.toItem(any(ItemUpdateRequest.class), anyLong())).thenReturn(new Item());
        when(itemService.update(anyLong(), anyLong(), any(Item.class))).thenReturn(new Item());
        when(itemMapper.toItemResponse(any(Item.class))).thenReturn(itemResponse);

        mvc.perform(patch("/items/{itemId}", ITEM_ID)
                        .header(USER_ID_HEADER, USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ITEM_ID));
    }

    @Test
    @SneakyThrows
    void get_ShouldReturnItem() {
        when(itemService.getById(anyLong(), anyLong())).thenReturn(itemResponse);

        mvc.perform(get("/items/{itemId}", ITEM_ID)
                        .header(USER_ID_HEADER, USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ITEM_ID));
    }

    @Test
    @SneakyThrows
    void getUserItems_ShouldReturnUserItems() {
        when(itemService.getUserItems(anyLong())).thenReturn(List.of(new Item()));
        when(itemMapper.toItemResponse(any(Item.class))).thenReturn(itemResponse);

        mvc.perform(get("/items")
                        .header(USER_ID_HEADER, USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(ITEM_ID));
    }

    @Test
    @SneakyThrows
    void search_ShouldReturnItems() {
        when(itemService.search(anyString())).thenReturn(List.of(new Item()));
        when(itemMapper.toItemResponse(any(Item.class))).thenReturn(itemResponse);

        mvc.perform(get("/items/search")
                        .param("text", "item"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(ITEM_ID));
    }

    @Test
    @SneakyThrows
    void addComment_ShouldAddComment() {
        when(commentMapper.toComment(any(CommentCreateRequest.class), anyLong(), anyLong()))
                .thenReturn(new Comment());
        when(itemService.addComment(any(Comment.class))).thenReturn(new Comment());
        when(commentMapper.toCommentResponse(any(Comment.class))).thenReturn(commentResponse);

        mvc.perform(post("/items/{itemId}/comment", ITEM_ID)
                        .header(USER_ID_HEADER, USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("Comment"));
    }
}