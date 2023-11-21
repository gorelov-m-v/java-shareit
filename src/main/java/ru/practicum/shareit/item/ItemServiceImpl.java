package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static ru.practicum.shareit.item.ItemMapper.itemToItemResponseDto;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    @Override
    public ItemResponseDto addItem(User user, ItemRequestDto itemRequestDto) {
        return itemToItemResponseDto(itemRepository.add(itemRequestDto, user));
    }

    @Override
    public ItemResponseDto updateItem(User user, Long itemId, ItemRequestDto itemRequestDto) throws NotFoundException {
        return itemToItemResponseDto(itemRepository.update(itemRequestDto, user, itemId));
    }

    @Override
    public ItemResponseDto getItem(Long itemId) {
        return itemToItemResponseDto(itemRepository.findById(itemId));
    }

    @Override
    public List<ItemResponseDto> getUserItems(Long userId) {
        return null;
    }

    @Override
    public List<ItemResponseDto> searchItems(String text) {
        return null;
    }
}
