package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;
import java.util.List;
import java.util.stream.Collectors;
import static ru.practicum.shareit.item.ItemMapper.itemToItemResponseDto;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemResponseDto addItem(Long userId, ItemRequestDto itemRequestDto) {
        User user = userRepository.getUser(userId);
        return itemToItemResponseDto(itemRepository.add(itemRequestDto, user));
    }

    @Override
    public ItemResponseDto updateItem(Long userId, Long itemId, ItemRequestDto itemRequestDto) throws NotFoundException {
        User user = userRepository.getUser(userId);
        return itemToItemResponseDto(itemRepository.update(itemRequestDto, user, itemId));
    }

    @Override
    public ItemResponseDto getItem(Long itemId) {
        return itemToItemResponseDto(itemRepository.findById(itemId));
    }

    @Override
    public List<ItemResponseDto> getUserItems(Long userId) {
        if (userRepository.getUser(userId) != null) {
            return itemRepository.findItemsByUserId(userId).stream()
                    .map(ItemMapper::itemToItemResponseDto)
                    .collect(Collectors.toList());
        } else {
            throw new NotFoundException("Пользователь не найден");
        }

    }

    @Override
    public List<ItemResponseDto> searchItems(Long userId, String text) {
        if (userRepository.getUser(userId) == null) {
            throw new NotFoundException("Пользователь не найден");
        } else {
            return itemRepository.search(text.toLowerCase()).stream()
                    .map(ItemMapper::itemToItemResponseDto)
                    .collect(Collectors.toList());
        }
    }
}
