package ru.practicum.shareit.item;

import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemWithCommentResponseDto;
import ru.practicum.shareit.item.dto.ItemWithRequestResponseDto;

import java.util.List;

public interface ItemService {

    ItemWithRequestResponseDto add(Long userId, ItemWithRequestResponseDto itemWithRequestResponseDto);

    ItemWithRequestResponseDto update(Long userId, Long itemId, ItemWithRequestResponseDto itemWithRequestResponseDto) throws NotFoundException;

    ItemWithCommentResponseDto get(Long userId, Long itemId);

    List<ItemWithCommentResponseDto> getUserItems(Long userId, PageRequest page);

    List<ItemWithCommentResponseDto> searchItems(Long userId, String text);

    CommentResponseDto addComment(Long userId, Long itemId, CommentRequestDto commentRequestDto);
}
