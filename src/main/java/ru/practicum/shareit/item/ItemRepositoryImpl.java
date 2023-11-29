package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class ItemRepositoryImpl implements ItemRepository {

    protected static Long id = 0L;
    private Map<Long, Item> items = new HashMap<>();

    @Override
    public Item add(ItemRequestDto itemDtoRequest, User user) {
        Item item = ItemMapper.itemFromItemRequestDto(itemDtoRequest, user);
        items.put(item.getId(),item);

        return item;
    }

    @Override
    public Item update(ItemRequestDto itemDtoRequest, User user, Long itemId) {
        Item updatedItem = items.get(itemId);

        if (!updatedItem.getOwner().getId().equals(user.getId()))
            throw new NotFoundException("Доступно только владельцу");

        if (itemDtoRequest.getName() != null)
            updatedItem.setName(itemDtoRequest.getName());

        if (itemDtoRequest.getDescription() != null)
            updatedItem.setDescription(itemDtoRequest.getDescription());

        if (itemDtoRequest.getAvailable() != null)
            updatedItem.setAvailable(itemDtoRequest.getAvailable());


        items.put(updatedItem.getId(), updatedItem);

        return  updatedItem;
    }

    @Override
    public Item findById(Long itemId) {
        return items.get(itemId);
    }

    @Override
    public List<Item> findItemsByUserId(Long ownerId) {
        return items.values().stream()
                .filter(item -> item.getOwner().getId().equals(ownerId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> search(String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        } else {
            return items.values()
                    .stream()
                    .filter(i -> isMatch(i, text))
                    .collect(Collectors.toList());
        }
    }

    private static boolean isMatch(Item item, String text) {
        return Boolean.TRUE.equals(item.getAvailable())
                && (item.getName().toLowerCase().contains(text) || item.getDescription().toLowerCase().contains(text));
    }

    protected static Long generateId() {
        return ++ id;
    }
}
