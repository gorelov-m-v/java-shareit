package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemWithRequestResponseDto;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build());
    }

    public ResponseEntity add(Long userId, ItemWithRequestResponseDto itemDto) {
        return post("", userId, itemDto);
    }

    public ResponseEntity update(long userId, long itemId, ItemWithRequestResponseDto itemDto) {
        return patch("/" + itemId, userId, itemDto);
    }

    public ResponseEntity get(long userId, long itemId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity getUserItems(long userId, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("?from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity searchItems(long userId, String text) {
        return get("/search?text=" + text, userId);
    }

    public ResponseEntity addComment(long userId, long itemId, CommentRequestDto commentDto) {
        return post("/" + itemId + "/comment", userId, commentDto);
    }
}