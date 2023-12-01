package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.item.ItemMapper.itemToItemResponseDto;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

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
        List<Comment> comments = commentRepository.findByItem_Id(itemId);

        ItemResponseDto itemResponseDto = ItemMapper.itemToItemResponseDto(item);
        itemResponseDto.setComments(comments.stream()
                .map(CommentMapper::commentToCommentResponseDto)
                .collect(Collectors.toList()));

        return itemResponseDto;
    }

    @Override
    public List<ItemResponseDto> getUserItems(Long userId) {
        findUserIfExists(userId);

        return itemRepository.findByOwnerId(userId)
                .stream()
                .map(ItemMapper::itemToItemResponseDto)
                .peek(itemResponseDto -> itemResponseDto.setComments(
                        commentRepository.findByItem_Id(itemResponseDto.getId())
                                .stream()
                                .map(CommentMapper::commentToCommentResponseDto)
                                .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemResponseDto> searchItems(Long userId, String text) {
        findUserIfExists(userId);

        if (text.isEmpty())
            return new ArrayList<>();

        return itemRepository.findItemsByText(text).stream()
                .map(ItemMapper::itemToItemResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentResponseDto addComment(Long userId, Long itemId, CommentRequestDto commentRequestDto) {
        User user = findUserIfExists(userId);
        Item item = findItemIfExists(itemId);

        Comment comment = commentRepository.save(CommentMapper.commentRequestDtoToComment(
                commentRequestDto, user, item));

        return CommentMapper.commentToCommentResponseDto(comment);
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
