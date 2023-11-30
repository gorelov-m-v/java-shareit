package ru.practicum.shareit.item;

import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;

import java.util.List;

public interface ItemService {

    ItemResponseDto add(Long userId, ItemRequestDto itemRequestDto);

    ItemResponseDto update(Long userId, Long itemId, ItemRequestDto itemRequestDto) throws NotFoundException;

    ItemResponseDto get(Long itemId);

    List<ItemResponseDto> getUserItems(Long userId);

    List<ItemResponseDto> searchItems(Long userId, String text);
}
