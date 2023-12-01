package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public class ItemMapper {
    public static Item itemFromItemRequestDto(ItemRequestDto itemRequestDto, User user) {
        return new Item(
                itemRequestDto.getName(),
                itemRequestDto.getDescription(),
                itemRequestDto.getAvailable(),
                user);
    }

    public static ItemResponseDto itemToItemResponseDto(Item item) {
        return new ItemResponseDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable());
    }
}
