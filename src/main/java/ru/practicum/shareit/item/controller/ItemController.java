package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemWithCommentResponseDto;
import ru.practicum.shareit.item.dto.ItemWithRequestResponseDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.validator.Create;
import ru.practicum.shareit.validator.Update;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/items")
@Validated
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemWithRequestResponseDto add(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @RequestBody @Validated({Create.class})
                                          ItemWithRequestResponseDto itemWithRequestResponseDto) {
        ItemWithRequestResponseDto withRequestResponseDto = itemService.add(userId, itemWithRequestResponseDto);
        return withRequestResponseDto;
    }

    @PatchMapping("/{itemId}")
    public ItemWithRequestResponseDto update(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @PathVariable("itemId") Long itemId,
                                             @RequestBody @Validated({Update.class})
                                             ItemWithRequestResponseDto itemWithRequestResponseDto) {
        ItemWithRequestResponseDto withRequestResponseDto = itemService.update(userId, itemId, itemWithRequestResponseDto);

        return withRequestResponseDto;
    }

    @GetMapping("/{itemId}")
    public ItemWithCommentResponseDto get(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @PathVariable("itemId") Long itemId) {
        ItemWithCommentResponseDto itemWithCommentResponseDto = itemService.get(userId, itemId);
        return itemWithCommentResponseDto;
    }

    @GetMapping
    public List<ItemWithCommentResponseDto> getUserItems(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                         @RequestParam(defaultValue = "0") @Min(0) int from,
                                                         @RequestParam(defaultValue = "10") @Min(1) int size) {
        PageRequest page = PageRequest.of(from / size, size);
        List<ItemWithCommentResponseDto> foundItems = itemService.getUserItems(userId, page);
        return foundItems;
    }

    @GetMapping("/search")
    public List<ItemWithCommentResponseDto> searchItems(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam String text) {
        List<ItemWithCommentResponseDto> foundItems = itemService.searchItems(userId, text);
        return foundItems;
    }

    @PostMapping("/{itemId}/comment")
    public CommentResponseDto addComment(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable("itemId") Long itemId,
                                         @RequestBody @Validated CommentRequestDto commentRequestDto) {
        CommentResponseDto commentResponseDto = itemService.addComment(userId, itemId, commentRequestDto);
        return commentResponseDto;
    }
}
