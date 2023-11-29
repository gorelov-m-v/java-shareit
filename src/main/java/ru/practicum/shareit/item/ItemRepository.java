package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemRepository {
    Item add(ItemRequestDto itemDtoRequest, User user);

    Item findById(Long itemId);

    Item update(ItemRequestDto itemDtoRequest, User user, Long itemId);

    List<Item> findItemsByUserId(Long ownerId);

    List<Item> search(String text);
}