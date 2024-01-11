package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestShortDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@Validated
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestShortDto addRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @RequestBody @Validated ItemRequestShortDto requestDto) {
        return itemRequestService.addRequest(userId, requestDto);
    }

    @GetMapping
    public List<ItemRequestDto> getUserItemRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.getUserItemRequests(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllItemRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                  @RequestParam(defaultValue = "0") @Min(value = 0) Integer from,
                                                  @RequestParam(defaultValue = "10") @Min(value = 1) Integer size) {
        PageRequest page = PageRequest.of(from / size, size)
                .withSort(Sort.by(Sort.Direction.DESC, "created"));
        return itemRequestService.getOtherItemRequests(userId, page);
    }

    @GetMapping("/{itemRequestId}")
    public ItemRequestDto getItemRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @PathVariable("itemRequestId") Long itemRequestId) {
        return itemRequestService.getItemRequest(userId, itemRequestId);
    }
}