package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemWithRequestResponseDto;
import ru.practicum.shareit.validator.Create;
import ru.practicum.shareit.validator.Update;

import javax.validation.constraints.Min;

@RestController
@RequestMapping("/items")
@Validated
@RequiredArgsConstructor
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity add(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @RequestBody @Validated({Create.class})
                              ItemWithRequestResponseDto itemWithRequestResponseDto) {
        return itemClient.add(userId, itemWithRequestResponseDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity update(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @PathVariable("itemId") Long itemId,
                                 @RequestBody @Validated({Update.class})
                                 ItemWithRequestResponseDto itemWithRequestResponseDto) {

        return itemClient.update(userId, itemId, itemWithRequestResponseDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity get(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @PathVariable("itemId") Long itemId) {
        return itemClient.get(userId, itemId);
    }

    @GetMapping
    public ResponseEntity getUserItems(@RequestHeader("X-Sharer-User-Id") Long userId,
                                       @RequestParam(defaultValue = "0") @Min(0) int from,
                                       @RequestParam(defaultValue = "10") @Min(1) int size) {
        return itemClient.getUserItems(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity searchItems(@RequestHeader("X-Sharer-User-Id") Long userId,
                                      @RequestParam String text) {
        return itemClient.searchItems(userId, text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity addComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @PathVariable("itemId") Long itemId,
                                     @RequestBody @Validated CommentRequestDto commentRequestDto) {
        return itemClient.addComment(userId, itemId, commentRequestDto);
    }
}
