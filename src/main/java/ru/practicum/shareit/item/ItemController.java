package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.validator.Create;
import ru.practicum.shareit.validator.Update;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/items")
@Slf4j
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemResponseDto add(@RequestHeader("X-Sharer-User-Id") Long userId,
                               @RequestBody @Validated({Create.class}) ItemRequestDto itemRequestDto,
                               HttpServletRequest request, HttpServletResponse response) {
        log.debug("Request - {} {} Body: {}", request.getMethod(), request.getRequestURI(), itemRequestDto);
        ItemResponseDto itemResponseDto = itemService.add(userId, itemRequestDto);
        log.debug("Response - StatusCode: {} Body: {},", response.getStatus(), itemResponseDto);
        return itemResponseDto;
    }

    @PatchMapping("/{itemId}")
    public ItemResponseDto update(@RequestHeader("X-Sharer-User-Id") Long userId,
                                  @PathVariable("itemId") Long itemId,
                                  @RequestBody @Validated({Update.class}) ItemRequestDto itemRequestDto,
                                  HttpServletRequest request, HttpServletResponse response) {
        log.debug("Request - {} {} Body: {}", request.getMethod(), request.getRequestURI(), itemRequestDto);
        ItemResponseDto itemResponseDto = itemService.update(userId, itemId, itemRequestDto);
        log.debug("Response - StatusCode: {} Body: {},", response.getStatus(), itemResponseDto);
        return itemResponseDto;
    }

    @GetMapping("/{itemId}")
    public ItemResponseDto get(@RequestHeader("X-Sharer-User-Id") Long userId,
                               @PathVariable("itemId") Long itemId,
                               HttpServletRequest request, HttpServletResponse response) {
        log.debug("Request - {} {}", request.getMethod(), request.getRequestURI());
        ItemResponseDto itemResponseDto = itemService.get(userId, itemId);
        log.debug("Response - StatusCode: {} Body: {},", response.getStatus(), itemResponseDto);
        return itemResponseDto;
    }

    @GetMapping
    public List<ItemResponseDto> getUserItems(@RequestHeader("X-Sharer-User-Id") Long userId,
                                              HttpServletRequest request, HttpServletResponse response) {
        log.debug("Request - {} {}", request.getMethod(), request.getRequestURI());
        List<ItemResponseDto> foundItems = itemService.getUserItems(userId);
        log.debug("Response - StatusCode: {} Body: {},", response.getStatus(), foundItems);
        return foundItems;
    }

    @GetMapping("/search")
    public List<ItemResponseDto> searchItems(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam String text,
                                             HttpServletRequest request, HttpServletResponse response) {
        log.debug("Request - {} {}", request.getMethod(), request.getRequestURI());
        List<ItemResponseDto> foundItems = itemService.searchItems(userId, text);
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
