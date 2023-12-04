package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

public class CommentMapper {
    public static Comment commentRequestDtoToComment(CommentRequestDto commentRequestDto, User author, Item item) {
        return new Comment(commentRequestDto.getText(), item, author, LocalDateTime.now());
    }

    public static CommentResponseDto commentToCommentResponseDto(Comment comment) {
        return new CommentResponseDto(
                comment.getId(),
                comment.getText(),
                comment.getAuthor().getName(),
                comment.getCreated());
    }
}
