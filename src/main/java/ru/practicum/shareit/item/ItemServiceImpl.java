package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.InvalidArgumentException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemWithCommentResponseDto;
import ru.practicum.shareit.item.dto.ItemWithRequestResponseDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.item.ItemMapper.toItemShortDto;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final ItemRequestRepository itemRequestRepository;

    @Override
    public ItemWithRequestResponseDto add(Long userId, ItemWithRequestResponseDto itemWithRequestResponseDto) {
        User user = findUserIfExists(userId);
        Long requestId = itemWithRequestResponseDto.getRequestId();
        ItemRequest itemRequest = null;

        if (requestId != null)
            itemRequest = itemRequestRepository.findById(requestId)
                    .orElseThrow(() -> new NotFoundException("Запрос с id = " + requestId + " не найден."));

        Item item = ItemMapper.itemShortDtoToItem(itemWithRequestResponseDto, user, itemRequest);

        item = itemRepository.save(item);
        return toItemShortDto(item);
    }

    @Override
    public ItemWithRequestResponseDto update(Long userId, Long itemId, ItemWithRequestResponseDto itemWithRequestResponseDto) throws NotFoundException {
        User user = findUserIfExists(userId);
        Item item = findItemIfExists(itemId);

        if (!item.getOwner().getId().equals(user.getId()))
            throw new NotFoundException("Item with id = " + itemId + " not available to the user with id " + userId);

        if (itemWithRequestResponseDto.getName() != null)
            item.setName(itemWithRequestResponseDto.getName());

        if (itemWithRequestResponseDto.getDescription() != null)
            item.setDescription(itemWithRequestResponseDto.getDescription());

        if (itemWithRequestResponseDto.getAvailable() != null)
            item.setAvailable(itemWithRequestResponseDto.getAvailable());

        item = itemRepository.save(item);

        return toItemShortDto(item);
    }

    @Override
    public ItemWithCommentResponseDto get(Long userId, Long itemId) {
        Item item = findItemIfExists(itemId);
        List<Comment> comments = commentRepository.findByItem_Id(itemId);

        ItemWithCommentResponseDto itemWithCommentResponseDto = ItemMapper.itemToItemResponseDto(item);
        itemWithCommentResponseDto.setComments(comments.stream()
                .map(CommentMapper::commentToCommentResponseDto)
                .collect(Collectors.toList()));

        if (item.getOwner().getId().equals(userId))
            setBookingToItemResponseDto(itemWithCommentResponseDto);

        return itemWithCommentResponseDto;
    }

    @Override
    public List<ItemWithCommentResponseDto> getUserItems(Long userId, PageRequest page) {
        findUserIfExists(userId);

        return itemRepository.findByOwnerId(userId, page)
                .stream()
                .map(ItemMapper::itemToItemResponseDto)
                .peek(itemResponseDto -> itemResponseDto.setComments(
                        commentRepository.findByItem_Id(itemResponseDto.getId())
                                .stream()
                                .map(CommentMapper::commentToCommentResponseDto)
                                .collect(Collectors.toList())))
                .peek(this::setBookingToItemResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemWithCommentResponseDto> searchItems(Long userId, String text) {
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

        List<Booking> bookingList = bookingRepository.findByItem_IdAndBooker_IdAndEndBefore(itemId, userId,
                LocalDateTime.now());

        if (bookingList.isEmpty())
            throw new InvalidArgumentException("The booker can make a comment after the end of the rental.");

        Comment comment = commentRepository.save(CommentMapper.commentRequestDtoToComment(
                commentRequestDto, user, item));

        return CommentMapper.commentToCommentResponseDto(comment);
    }

    private ItemWithCommentResponseDto setBookingToItemResponseDto(ItemWithCommentResponseDto itemWithCommentResponseDto) {
        List<Booking> bookings = bookingRepository.findBookingsByItemAsc(itemWithCommentResponseDto.getId());

        Booking nextBooking = bookings
                .stream()
                .filter(b -> b.getStart().isAfter(LocalDateTime.now()) && !BookingStatus.REJECTED.equals(b.getStatus()))
                .min(Comparator.comparing(Booking::getStart))
                .orElse(null);

        Booking lastBooking = bookings
                .stream()
                .filter(b -> b.getStart().isBefore(LocalDateTime.now()) && !BookingStatus.REJECTED.equals(b.getStatus()))
                .min((b1, b2) -> b2.getStart().compareTo(b1.getStart()))
                .orElse(null);

        if (lastBooking != null)
            itemWithCommentResponseDto.setLastBooking(BookingMapper.bookingToBookingShortDto(lastBooking));

        if (nextBooking != null)
            itemWithCommentResponseDto.setNextBooking(BookingMapper.bookingToBookingShortDto(nextBooking));

        return itemWithCommentResponseDto;
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
