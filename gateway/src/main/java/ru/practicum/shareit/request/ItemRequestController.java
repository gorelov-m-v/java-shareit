package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.dto.ItemRequestShortDto;

import javax.validation.constraints.Min;

@RestController
@RequestMapping(path = "/requests")
@Validated
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity addRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @RequestBody @Validated ItemRequestShortDto requestDto) {
        return itemRequestClient.addRequest(userId, requestDto);
    }

    @GetMapping
    public ResponseEntity getUserItemRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestClient.getUserItemRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity getAllItemRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                            @RequestParam(defaultValue = "0") @Min(value = 0) Integer from,
                                            @RequestParam(defaultValue = "10") @Min(value = 1) Integer size) {
        return itemRequestClient.getOtherItemRequests(userId, from, size);
    }

    @GetMapping("/{itemRequestId}")
    public ResponseEntity getItemRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @PathVariable("itemRequestId") Long itemRequestId) {
        return itemRequestClient.getItemRequest(userId, itemRequestId);
    }
}