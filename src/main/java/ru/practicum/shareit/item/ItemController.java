package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemWithCommentResponseDto;
import ru.practicum.shareit.item.dto.ItemWithRequestResponseDto;
import ru.practicum.shareit.validator.Create;
import ru.practicum.shareit.validator.Update;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/items")
@Slf4j
@Validated
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemWithRequestResponseDto add(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @RequestBody @Validated({Create.class}) ItemWithRequestResponseDto itemWithRequestResponseDto,
                                          HttpServletRequest request, HttpServletResponse response) {
        log.debug("Request - {} {} Body: {}", request.getMethod(), request.getRequestURI(), itemWithRequestResponseDto);
        ItemWithRequestResponseDto withRequestResponseDto = itemService.add(userId, itemWithRequestResponseDto);
        log.debug("Response - StatusCode: {} Body: {},", response.getStatus(), withRequestResponseDto);
        return withRequestResponseDto;
    }

    @PatchMapping("/{itemId}")
    public ItemWithRequestResponseDto update(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @PathVariable("itemId") Long itemId,
                                             @RequestBody @Validated({Update.class}) ItemWithRequestResponseDto itemWithRequestResponseDto,
                                             HttpServletRequest request, HttpServletResponse response) {
        log.debug("Request - {} {} Body: {}", request.getMethod(), request.getRequestURI(), itemWithRequestResponseDto);
        ItemWithRequestResponseDto withRequestResponseDto = itemService.update(userId, itemId, itemWithRequestResponseDto);
        log.debug("Response - StatusCode: {} Body: {},", response.getStatus(), withRequestResponseDto);
        return withRequestResponseDto;
    }

    @GetMapping("/{itemId}")
    public ItemWithCommentResponseDto get(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @PathVariable("itemId") Long itemId,
                                          HttpServletRequest request, HttpServletResponse response) {
        log.debug("Request - {} {}", request.getMethod(), request.getRequestURI());
        ItemWithCommentResponseDto itemWithCommentResponseDto = itemService.get(userId, itemId);
        log.debug("Response - StatusCode: {} Body: {},", response.getStatus(), itemWithCommentResponseDto);
        return itemWithCommentResponseDto;
    }

    @GetMapping
    public List<ItemWithCommentResponseDto> getUserItems(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                         @RequestParam(defaultValue = "0") @Min(0) int from,
                                                         @RequestParam(defaultValue = "10") @Min(1) int size,
                                                         HttpServletRequest request, HttpServletResponse response) {
        log.debug("Request - {} {}", request.getMethod(), request.getRequestURI());
        PageRequest page = PageRequest.of(from / size, size);
        List<ItemWithCommentResponseDto> foundItems = itemService.getUserItems(userId, page);
        log.debug("Response - StatusCode: {} Body: {},", response.getStatus(), foundItems);
        return foundItems;
    }

    @GetMapping("/search")
    public List<ItemWithCommentResponseDto> searchItems(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam String text,
                                                        HttpServletRequest request, HttpServletResponse response) {
        log.debug("Request - {} {}", request.getMethod(), request.getRequestURI());
        List<ItemWithCommentResponseDto> foundItems = itemService.searchItems(userId, text);
        log.debug("Response - StatusCode: {} Body: {},", response.getStatus(), foundItems);
        return foundItems;
    }

    @PostMapping("/{itemId}/comment")
    public CommentResponseDto addComment(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable("itemId") Long itemId,
                                         @RequestBody @Validated CommentRequestDto commentRequestDto,
                                         HttpServletRequest request, HttpServletResponse response) {
        log.debug("Request - {} {}", request.getMethod(), request.getRequestURI());
        CommentResponseDto commentResponseDto = itemService.addComment(userId, itemId, commentRequestDto);
        log.debug("Response - StatusCode: {} Body: {},", response.getStatus(), commentResponseDto);
        return commentResponseDto;
    }
}
