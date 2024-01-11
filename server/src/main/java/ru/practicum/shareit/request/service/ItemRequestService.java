package ru.practicum.shareit.request.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestShortDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestShortDto addRequest(Long userId, ItemRequestShortDto requestDto);

    List<ItemRequestDto> getUserItemRequests(Long userId);

    List<ItemRequestDto> getOtherItemRequests(Long userId, PageRequest page);

    ItemRequestDto getItemRequest(Long userId, Long itemRequestId);
}
