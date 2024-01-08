package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.InvalidArgumentException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)

public class BookingServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private BookingRepository bookingRepository;
    private User user = createUser();
    private BookingRequestDto bookingRequestDto = new BookingRequestDto(
            LocalDateTime.now(), LocalDateTime.now().plusDays(2), 1L);
    private BookingService bookingService;

    @BeforeEach
    void initBookingService() {
        bookingService = new BookingServiceImpl(bookingRepository, itemRepository, userRepository);
    }

    @Test
    void addBooking_WithWrongUser_Test() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        NotFoundException result = assertThrows(NotFoundException.class,
                () -> bookingService.create(bookingRequestDto, 1L));
        assertEquals(result.getMessage(), "User with  id = 1 not found.");
    }

    @Test
    void addBooking_WithWrongItem_Test() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());

        NotFoundException result = assertThrows(NotFoundException.class,
                () -> bookingService.create(bookingRequestDto, 1L));
        assertEquals(result.getMessage(), "Item with  id = 1 not found.");
    }

    @Test
    void addBooking_WithNotAvailableItem_Test() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        Item item = createItem();
        item.setAvailable(false);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        InvalidArgumentException result = assertThrows(InvalidArgumentException.class,
                () -> bookingService.create(bookingRequestDto, 2L));
        assertEquals(result.getMessage(), "Item with id 1 is not available.");
    }

    @Test
    void addBooking_WithWrongTime_Test() {
        Item item = createItem();
        item.setAvailable(true);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        BookingRequestDto bookingRequestDto = new BookingRequestDto(
                LocalDateTime.now(), LocalDateTime.now().minusDays(2), 1L);

        InvalidArgumentException result = assertThrows(InvalidArgumentException.class,
                () -> bookingService.create(bookingRequestDto, 2L));
        assertEquals(result.getMessage(), "end can't be earlier than start.");
    }

    @Test
    void approveOrRejectBooking_ByWrongUser_Test() {
        Booking booking = createBooking(BookingStatus.WAITING);
        booking.setItem(createItem());
        booking.setBooker(user);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        NotFoundException result = assertThrows(NotFoundException.class,
                () -> bookingService.update(1L, 2L, true));
        assertEquals(result.getMessage(), "This is not your item.");
    }

    @Test
    void approveOrRejectBooking_WithWrongStatus_Test() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        Booking booking = createBooking(BookingStatus.APPROVED);
        booking.setItem(createItem());
        booking.setBooker(user);
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        InvalidArgumentException result = assertThrows(InvalidArgumentException.class,
                () -> bookingService.update(1L, 1L, true));
        assertEquals(result.getMessage(), "Only available for status WAITING");
    }


    @Test
    void approveOrRejectBooking_Approve_Test() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        Booking booking = createBooking(BookingStatus.WAITING);
        booking.setItem(createItem());
        booking.setBooker(new User());
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        bookingService.update(1L, 1L, true);

        Mockito.verify(bookingRepository, Mockito.times(1))
                .save(booking);
    }

    @Test
    void approveOrRejectBooking_Reject_Test() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        Booking booking = createBooking(BookingStatus.WAITING);
        booking.setItem(createItem());
        booking.setBooker(new User());
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        bookingService.update(1L, 1L, false);

        Mockito.verify(bookingRepository, Mockito.times(1))
                .save(booking);
    }

    @Test
    void getBooking_ForbiddenException_Test() {
        Booking booking = createBookingWithItemAndBooker();
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));

        NotFoundException result = assertThrows(NotFoundException.class,
                () -> bookingService.get(1L, 4L));
        assertEquals(result.getMessage(), "Only available to owner or booker");
    }

    @Test
    void getBooking_BookingFound_Test() {
        Booking booking = createBookingWithItemAndBooker();
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));

        BookingResponseDto result = bookingService.get(1L, booking.getId());

        assertEquals(booking.getId(), result.getId());
    }

    @Test
    void getBookingsForAllUserItems_BookingStateAll_Test() {
        Booking booking = createBookingWithItemAndBooker();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(booking.getBooker()));
        when(bookingRepository.findAllByBooker(any(), any())).thenReturn(List.of(booking));


        List<BookingResponseDto> resultBookings = bookingService.getAll(2L, "ALL",
                PageRequest.of(1, 10));

        assertEquals(1, resultBookings.size());
        assertEquals(booking.getId(), resultBookings.get(0).getId());
    }

    @Test
    void getBookingsForAllUserItems_BookingStateCurrent_Test() {
        Booking booking = createBookingWithItemAndBooker();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(bookingRepository.findAllByBookerAndStartBeforeAndEndAfter(any(), any(), any(), any())).thenReturn(List.of(booking));

        List<BookingResponseDto> resultBookings = bookingService.getAll(2L, "CURRENT",
                PageRequest.of(1, 10));

        assertEquals(1, resultBookings.size());
        assertEquals(booking.getId(), resultBookings.get(0).getId());
    }

    @Test
    void getBookingsForAllUserItems_BookingStatePast_Test() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        Booking booking = createBookingWithItemAndBooker();
        when(bookingRepository.findAllByBookerAndEndBefore(any(), any(), any())).thenReturn(List.of(booking));

        List<BookingResponseDto> resultBookings = bookingService.getAll(1L, "PAST",
                PageRequest.of(1, 10));

        assertEquals(1, resultBookings.size());
        assertEquals(booking.getId(), resultBookings.get(0).getId());
    }

    @Test
    void getBookingsForAllUserItems_BookingStateFuture_Test() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        Booking booking = createBookingWithItemAndBooker();
        when(bookingRepository.findAllByBookerAndStartAfter(any(), any(), any())).thenReturn(List.of(booking));

        List<BookingResponseDto> resultBookings = bookingService.getAll(11L, "FUTURE",
                PageRequest.of(1, 10));

        assertEquals(1, resultBookings.size());
        assertEquals(booking.getId(), resultBookings.get(0).getId());
    }

    @Test
    void getBookingsForAllUserItems_BookingStateRejected_Test() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        Booking booking = createBookingWithItemAndBooker();
        when(bookingRepository.findAllByBookerAndStatusEquals(any(), any(), any())).thenReturn(List.of(booking));

        List<BookingResponseDto> resultBookings = bookingService.getAll(11L, "REJECTED",
                PageRequest.of(1, 10));

        assertEquals(1, resultBookings.size());
        assertEquals(booking.getId(), resultBookings.get(0).getId());
    }

    @Test
    void getBookingsForAllUserItems_BookingStateWaiting_Test() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        Booking booking = createBookingWithItemAndBooker();
        when(bookingRepository.findAllByBookerAndStatusEquals(any(), any(), any())).thenReturn(List.of(booking));

        List<BookingResponseDto> resultBookings = bookingService.getAll(11L, "WAITING",
                PageRequest.of(1, 10));

        assertEquals(1, resultBookings.size());
        assertEquals(booking.getId(), resultBookings.get(0).getId());
    }

    @Test
    void getUserBookings_BookingStateCurrent_Test() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        Booking booking = createBookingWithItemAndBooker();
        when(bookingRepository.findAllByItemOwnerAndStartBeforeAndEndAfter(any(), any(), any(), any())).thenReturn(List.of(booking));

        List<BookingResponseDto> resultBookings = bookingService.getOwnerItemsAll(1L, "CURRENT",
                PageRequest.of(0, 10));

        assertEquals(1, resultBookings.size());
        assertEquals(booking.getId(), resultBookings.get(0).getId());
    }

    @Test
    void getUserBookings_BookingStatePast_Test() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        Booking booking = createBookingWithItemAndBooker();
        when(bookingRepository.findAllByItemOwnerAndEndBefore(any(), any(), any())).thenReturn(List.of(booking));

        List<BookingResponseDto> resultBookings = bookingService.getOwnerItemsAll(1L, "PAST",
                PageRequest.of(0, 10));

        assertEquals(1, resultBookings.size());
        assertEquals(booking.getId(), resultBookings.get(0).getId());
    }

    @Test
    void getUserBookings_BookingStateFuture_Test() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        Booking booking = createBookingWithItemAndBooker();
        when(bookingRepository.findAllByItemOwnerAndStartAfter(any(), any(), any())).thenReturn(List.of(booking));

        List<BookingResponseDto> resultBookings = bookingService.getOwnerItemsAll(1L, "FUTURE",
                PageRequest.of(0, 10));

        assertEquals(1, resultBookings.size());
        assertEquals(booking.getId(), resultBookings.get(0).getId());
    }

    @Test
    void getUserBookings_BookingStateRejected_Test() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        Booking booking = createBookingWithItemAndBooker();
        when(bookingRepository.findAllByItemOwnerAndStatusEquals(any(), any(), any())).thenReturn(List.of(booking));

        List<BookingResponseDto> resultBookings = bookingService.getOwnerItemsAll(1L, "REJECTED",
                PageRequest.of(0, 10));

        assertEquals(1, resultBookings.size());
        assertEquals(booking.getId(), resultBookings.get(0).getId());
    }

    @Test
    void getUserBookings_BookingStateWaiting_Test() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        Booking booking = createBookingWithItemAndBooker();
        when(bookingRepository.findAllByItemOwnerAndStatusEquals(any(), any(), any())).thenReturn(List.of(booking));

        List<BookingResponseDto> resultBookings = bookingService.getOwnerItemsAll(1L, "WAITING",
                PageRequest.of(0, 10));

        assertEquals(1, resultBookings.size());
        assertEquals(booking.getId(), resultBookings.get(0).getId());
    }

    @Test
    void getUserBookings_BookingStateAll_Test() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        Booking booking = createBookingWithItemAndBooker();
        when(bookingRepository.findAllByItemOwner(any(), any())).thenReturn(List.of(booking));

        List<BookingResponseDto> resultBookings = bookingService.getOwnerItemsAll(1L, "ALL",
                PageRequest.of(0, 10));

        assertEquals(1, resultBookings.size());
        assertEquals(booking.getId(), resultBookings.get(0).getId());
    }

    private Item createItem() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Name");
        item.setDescription("Description");
        item.setAvailable(true);
        item.setOwner(user);
        return item;
    }

    private Booking createBooking(LocalDateTime start, LocalDateTime end, BookingStatus status) {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStart(start);
        booking.setEnd(end);
        booking.setStatus(status);
        return booking;
    }

    private Booking createBooking(BookingStatus status) {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));
        booking.setStatus(status);
        return booking;
    }

    private Booking createBookingWithItemAndBooker() {
        User booker = new User();
        booker.setId(2L);

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));
        booking.setStatus(BookingStatus.APPROVED);
        booking.setItem(createItem());
        booking.setBooker(booker);

        return booking;
    }

    private User createUser() {
        User user = new User();
        user.setId(1L);
        return user;
    }
}