package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.practicum.shareit.item.ItemMapper.itemToItemResponseDto;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemResponseDto add(Long userId, ItemRequestDto itemRequestDto) {
        User user = findUserIfExists(userId);
        Item item = ItemMapper.itemFromItemRequestDto(itemRequestDto, user);

        item = itemRepository.save(item);
        return itemToItemResponseDto(item);
    }

    @Override
    public ItemResponseDto update(Long userId, Long itemId, ItemRequestDto itemRequestDto) throws NotFoundException {
        User user = findUserIfExists(userId);
        Item item = findItemIfExists(itemId);

        if (!item.getOwner().getId().equals(user.getId()))
            throw new NotFoundException("Item with id = " + itemId + " not available to the user with id " + userId);

        if (itemRequestDto.getName() != null)
            item.setName(itemRequestDto.getName());

        if (itemRequestDto.getDescription() != null)
            item.setDescription(itemRequestDto.getDescription());

        if (itemRequestDto.getAvailable() != null)
            item.setAvailable(itemRequestDto.getAvailable());

        item = itemRepository.save(item);

        return itemToItemResponseDto(item);
    }

    @Override
    public ItemResponseDto get(Long itemId) {
        Item item = findItemIfExists(itemId);

        return itemToItemResponseDto(item);
    }

    @Override
    public List<ItemResponseDto> getUserItems(Long userId) {
        findUserIfExists(userId);

        return itemRepository.findByOwnerId(userId)
                .stream()
                .map(ItemMapper::itemToItemResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemResponseDto> searchItems(Long userId, String text) {
        findUserIfExists(userId);

        List<ItemResponseDto> itemList = new ArrayList<>();
        if (text.isEmpty()) {
            return itemList;
        }

        List<Item> findByName = itemRepository.findItemByNameContainsIgnoreCase(text);
        List<Item> findByDescription = itemRepository.findItemByDescriptionContainsIgnoreCase(text);
        itemList = Stream.concat(findByDescription.stream(), findByName.stream())
                .distinct()
                .filter(Item::getAvailable)
                .map(ItemMapper::itemToItemResponseDto)
                .collect(Collectors.toList());

        return itemList;
    }

    private User findUserIfExists(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with  id = " + userId + " not found."));
    }

    private Item findItemIfExists(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item with  id = " + itemId + " not found."));
    }
}
