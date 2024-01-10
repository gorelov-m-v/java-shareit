package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemWithCommentResponseDto;
import ru.practicum.shareit.item.dto.ItemWithRequestResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ItemMapper {

    public static ItemWithCommentResponseDto itemToItemResponseDto(Item item) {
        return new ItemWithCommentResponseDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable());
    }

    public static ItemWithRequestResponseDto toItemShortDto(Item item) {
        Long requestId;
        if (item.getRequest() == null) {
            requestId = null;
        } else {
            requestId = item.getRequest().getId();
        }

        return new ItemWithRequestResponseDto(item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner().getId(),
                requestId);
    }

    public static List<ItemWithRequestResponseDto> toItemShortDtoList(List<Item> items) {
        if (items == null) return Collections.emptyList();
        return items.stream()
                .map(ItemMapper::toItemShortDto)
                .collect(Collectors.toList());
    }

    public static Item itemShortDtoToItem(ItemWithRequestResponseDto itemDto, User owner, ItemRequest itemRequest) {
        Item item = new Item();
        item.setOwner(owner);
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setId(item.getId());
        item.setRequest(itemRequest);
        return item;
    }
}
