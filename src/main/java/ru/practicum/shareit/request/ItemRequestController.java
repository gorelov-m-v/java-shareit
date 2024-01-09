package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestShortDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@Slf4j
@Validated
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestShortDto addRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @RequestBody @Validated ItemRequestShortDto requestDto,
                                          HttpServletRequest request, HttpServletResponse response) {
        log.debug("Request - {} {}", request.getMethod(), request.getRequestURI());
        ItemRequestShortDto responseDto = itemRequestService.addRequest(userId, requestDto);
        log.debug("Response - StatusCode: {} Body: {},", response.getStatus(), responseDto);
        return responseDto;
    }

    @GetMapping
    public List<ItemRequestDto> getUserItemRequests(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                    HttpServletRequest request, HttpServletResponse response) {
        log.debug("Request - {} {}", request.getMethod(), request.getRequestURI());
        List<ItemRequestDto> responseDtoList = itemRequestService.getUserItemRequests(userId);
        log.debug("Response - StatusCode: {} Body: {},", response.getStatus(), responseDtoList);
        return responseDtoList;
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllItemRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                  @RequestParam(defaultValue = "0") @Min(value = 0) Integer from,
                                                  @RequestParam(defaultValue = "10") @Min(value = 1) Integer size,
                                                  HttpServletRequest request, HttpServletResponse response) {
        log.debug("Request - {} {}", request.getMethod(), request.getRequestURI());
        PageRequest page = PageRequest.of(from / size, size)
                .withSort(Sort.by(Sort.Direction.DESC, "created"));
        List<ItemRequestDto> responseDtoList = itemRequestService.getOtherItemRequests(userId, page);
        log.debug("Response - StatusCode: {} Body: {},", response.getStatus(), responseDtoList);
        return responseDtoList;
    }

    @GetMapping("/{itemRequestId}")
    public ItemRequestDto getItemRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @PathVariable("itemRequestId") Long itemRequestId,
                                         HttpServletRequest request, HttpServletResponse response) {
        log.debug("Request - {} {}", request.getMethod(), request.getRequestURI());
        ItemRequestDto responseDto = itemRequestService.getItemRequest(userId, itemRequestId);
        log.debug("Response - StatusCode: {} Body: {},", response.getStatus(), responseDto);
        return responseDto;
    }
}