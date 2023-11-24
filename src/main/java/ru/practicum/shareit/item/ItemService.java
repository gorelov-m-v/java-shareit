package ru.practicum.shareit.item;

import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;

import java.util.List;

public interface ItemService {

    ItemResponseDto addItem(Long userId, ItemRequestDto itemRequestDto);

    ItemResponseDto updateItem(Long userId, Long itemId, ItemRequestDto itemRequestDto) throws NotFoundException;

    ItemResponseDto getItem(Long itemId);

    List<ItemResponseDto> getUserItems(Long userId);

    List<ItemResponseDto> searchItems(Long userId, String text);
}
