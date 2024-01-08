package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.InvalidArgumentException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemWithCommentResponseDto;
import ru.practicum.shareit.item.dto.ItemWithRequestResponseDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemServiceImplTest {

    private static User user;
    private static ItemWithRequestResponseDto itemWithRequestResponseDto;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private ItemRequestRepository itemRequestRepository;
    private ItemService itemService;

    private static User createUser() {
        User user = new User();
        user.setId(1L);

        return user;
    }

    @BeforeEach
    void initItemService() {
        itemService = new ItemServiceImpl(itemRepository, userRepository, commentRepository, bookingRepository,
                itemRequestRepository);
        user = createUser();
        itemWithRequestResponseDto = new ItemWithRequestResponseDto(
                1L, "testItemName", "testItemDescription", true, 1L, null);
    }

    @Test
    void addItem_ShouldCall_Save_Once_Test() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(createUser()));
        when(itemRepository.save(any())).thenReturn(createItemWithUser());

        itemService.add(1L, itemWithRequestResponseDto);

        verify(itemRepository, times(1)).save(any());
    }

    @Test
    void addItem_WithWrongUser_ShouldThrow_NotFoundException_Test() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        NotFoundException result = assertThrows(NotFoundException.class,
                () -> itemService.add(1L, itemWithRequestResponseDto));
        assertEquals(result.getMessage(), "User with  id = 1 not found.");
    }

    @Test
    void addItem_WithWrongItemRequest_ShouldThrow_NotFoundException_Test() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(createUser()));
        itemWithRequestResponseDto.setRequestId(1111L);

        NotFoundException result = assertThrows(NotFoundException.class,
                () -> itemService.add(1L, itemWithRequestResponseDto));
        assertEquals(result.getMessage(), "Запрос с id = 1111 не найден.");
    }

    @Test
    void getItem_WithComment_PositiveTest() {
        Item item = createItemWithUser();
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        Comment comment = new Comment();
        comment.setItem(item);
        comment.setId(1L);
        comment.setText("Text");
        comment.setCreated(LocalDateTime.now());
        comment.setAuthor(user);
        when(commentRepository.findByItem_Id(anyLong())).thenReturn(List.of(comment));

        ItemWithCommentResponseDto resultItem = itemService.get(user.getId(), item.getId());

        assertAll(
                () -> assertEquals(resultItem.getId(), item.getId()),
                () -> assertEquals(resultItem.getName(), item.getName()),
                () -> assertEquals(resultItem.getDescription(), item.getDescription()),
                () -> assertEquals(resultItem.getComments().size(), 1),
                () -> assertEquals(resultItem.getComments().get(0).getId(), comment.getId()),
                () -> assertEquals(resultItem.getComments().get(0).getText(), comment.getText()));
    }

    @Test
    void getItem_ByOwner_PositiveTest() {
        Item item = createItemWithUser();
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(commentRepository.findByItem_Id(anyLong())).thenReturn(List.of());

        User booker = new User();
        booker.setId(2L);

        Booking booking1 = new Booking();
        booking1.setId(1L);
        booking1.setItem(item);
        booking1.setStart(LocalDateTime.now().plusDays(1));
        booking1.setEnd(LocalDateTime.now().plusDays(2));
        booking1.setStatus(BookingStatus.APPROVED);
        booking1.setBooker(booker);

        Booking booking2 = new Booking();
        booking2.setId(1L);
        booking2.setItem(item);
        booking2.setStart(LocalDateTime.now().minusDays(3));
        booking2.setEnd(LocalDateTime.now().minusDays(2));
        booking2.setStatus(BookingStatus.APPROVED);
        booking2.setBooker(booker);

        when(bookingRepository.findBookingsByItemAsc(anyLong())).thenReturn(List.of(booking1, booking2));

        ItemWithCommentResponseDto resultItem = itemService.get(user.getId(), item.getId());

        assertAll(
                () -> assertEquals(resultItem.getId(), item.getId()),
                () -> assertEquals(resultItem.getName(), item.getName()),
                () -> assertEquals(resultItem.getDescription(), item.getDescription()),
                () -> assertNotNull(resultItem.getLastBooking()),
                () -> assertNotNull(resultItem.getNextBooking()),
                () -> assertEquals(resultItem.getLastBooking().getId(), booking2.getId()),
                () -> assertEquals(resultItem.getNextBooking().getId(), booking1.getId()));
    }

    @Test
    void getItem_ByNotOwner_PositiveTest() {
        User newUser = new User();
        newUser.setId(111L);

        Item item = createItemWithUser();
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        User booker = new User();
        booker.setId(2L);

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setItem(item);
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));
        booking.setStatus(BookingStatus.APPROVED);
        booking.setBooker(booker);

        ItemWithCommentResponseDto resultItem = itemService.get(newUser.getId(), item.getId());

        assertAll(
                () -> assertEquals(resultItem.getId(), item.getId()),
                () -> assertEquals(resultItem.getName(), item.getName()),
                () -> assertEquals(resultItem.getDescription(), item.getDescription()),
                () -> assertNull(resultItem.getLastBooking()),
                () -> assertNull(resultItem.getNextBooking()));
    }

    @Test
    void addComment_WithoutBooking_ShouldThrow_InvalidArgumentException_Test() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(createUser()));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(createItemWithUser()));
        when(bookingRepository.findByItem_IdAndBooker_IdAndEndBefore(anyLong(), anyLong(), any()))
                .thenReturn(new ArrayList<>());

        InvalidArgumentException result = assertThrows(InvalidArgumentException.class,
                () -> itemService.addComment(1L, 1L, new CommentRequestDto("Text")));
        assertEquals(result.getMessage(), "The booker can make a comment after the end of the rental.");
    }

    @Test
    void addComment_Should_Call_Save_Once_Test() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(createUser()));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(createItemWithUser()));
        when(bookingRepository.findByItem_IdAndBooker_IdAndEndBefore(anyLong(), anyLong(), any()))
                .thenReturn(List.of(new Booking()));
        Comment comment = new Comment();
        comment.setAuthor(new User());
        when(commentRepository.save(any())).thenReturn(comment);

        itemService.addComment(1L, 1L, new CommentRequestDto("text"));

        verify(commentRepository, times(1)).save(any());
    }

    @Test
    void updateItem_ItemNotFound_ShouldThrow_NotFoundException_Test() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(createUser()));

        NotFoundException result = assertThrows(NotFoundException.class,
                () -> itemService.update(1L, 1L, itemWithRequestResponseDto));
        assertEquals(result.getMessage(), "Item with  id = 1 not found.");
    }

    @Test
    void updateItem_ItemUpdated_Should_Call_Save_Once_Test() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(createUser()));
        Item item = createItemWithUser();
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(itemRepository.save(any())).thenReturn(item);

        itemService.update(1L, 1L, itemWithRequestResponseDto);

        verify(itemRepository, times(1)).save(item);
    }

    @Test
    void searchItems_TextIsBlank_PositiveTest() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(createUser()));

        List<ItemWithCommentResponseDto> resultItems = itemService.searchItems(1L, "");

        assertTrue(resultItems.isEmpty());
    }

    @Test
    void searchItems_FoundItems_PositiveTest() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(createUser()));
        Item item = createItemWithUser();
        when(itemRepository.findItemsByText(anyString())).thenReturn(List.of(item));

        List<ItemWithCommentResponseDto> resultItems = itemService.searchItems(1L, "text");

        assertAll(
                () -> assertEquals(1, resultItems.size()),
                () -> assertEquals(item.getId(), resultItems.get(0).getId()));
    }

    private Item createItemWithUser() {
        Item item = new Item();
        item.setId(1L);
        item.setName("testItemName");
        item.setDescription("testItemDescription");
        item.setAvailable(true);
        item.setOwner(user);

        return item;
    }
}