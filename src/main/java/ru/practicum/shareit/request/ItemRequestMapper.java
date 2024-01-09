package ru.practicum.shareit.request;

import ru.practicum.shareit.item.dto.ItemWithRequestResponseDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestShortDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public class ItemRequestMapper {

    public static ItemRequestShortDto itemRequestToItemRequestShortDto(ItemRequest itemRequest) {
        return new ItemRequestShortDto(itemRequest.getId(), itemRequest.getDescription(), itemRequest.getCreated());
    }

    public static ItemRequestDto itemRequestToItemRequestResponseDto(ItemRequest itemRequest, List<ItemWithRequestResponseDto> items) {
        return new ItemRequestDto(itemRequest.getId(), itemRequest.getDescription(), itemRequest.getCreated(), items);
    }

    public static ItemRequest itemRequestDtoToItemRequest(ItemRequestShortDto itemRequestDto, User requestor) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(itemRequestDto.getDescription());
        itemRequest.setRequestor(requestor);
        return itemRequest;
    }
}
