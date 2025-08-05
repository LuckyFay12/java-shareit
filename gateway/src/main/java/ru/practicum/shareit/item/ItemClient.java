package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentCreateRequest;
import ru.practicum.shareit.item.dto.ItemCreateRequest;
import ru.practicum.shareit.item.dto.ItemUpdateRequest;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> create(Long userId, ItemCreateRequest request) {
        return post("", userId, request);
    }

    public ResponseEntity<Object> update(Long userId, Long itemId, ItemUpdateRequest request) {
        return patch("/" + itemId, userId, request);
    }

    public ResponseEntity<Object> getById(Long userId, Long itemId) {
        return get("/" + userId, itemId);
    }

    public ResponseEntity<Object> getUserItems(Long userId) {
        return get("/", userId);
    }

    public ResponseEntity<Object> search(String text) {
        String path = UriComponentsBuilder.fromPath("/search")
                .queryParam("text", text)
                .toUriString();
        return get(path);
    }

    public ResponseEntity<Object> createComment(Long userId, Long itemId, CommentCreateRequest request) {
        return post("/" + itemId + "/comment", userId, request);
    }
}