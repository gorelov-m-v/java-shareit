package ru.practicum.shareit.item;

import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemService {

    ItemResponseDto addItem(User user, ItemRequestDto itemRequestDto);

    ItemResponseDto updateItem(User user, Long itemId, ItemRequestDto itemRequestDto) throws NotFoundException;

    ItemResponseDto getItem(Long itemId);

    List<ItemResponseDto> getUserItems(Long userId);

    List<ItemResponseDto> searchItems(String text);
}
