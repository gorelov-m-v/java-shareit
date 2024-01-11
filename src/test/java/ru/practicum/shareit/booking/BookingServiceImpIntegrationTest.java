package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceImpIntegrationTest {
    @Autowired
    private BookingService bookingService;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;

    private Booking booking;
    private User booker;
    private Item item;

    @BeforeEach
    void saveBookings() {
        booker = new User();
        booker.setId(1L);
        booker.setName("testUserName");
        booker.setEmail("testUserEmail@email.com");
        booker = userRepository.save(booker);

        item = new Item();
        item.setId(1L);
        item.setName("Name");
        item.setDescription("Description");
        item.setAvailable(true);

        User owner = new User();
        owner.setEmail("owner@mail.ru");
        owner.setName("testOwnerName");
        item.setOwner(owner);
        userRepository.save(owner);
        item = itemRepository.save(item);

        booking = new Booking();
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now().plusDays(2));
        booking.setStatus(BookingStatus.APPROVED);
        booking.setBooker(booker);
        booking.setItem(item);
        booking = bookingRepository.save(booking);
    }

    @Test
    void testGetUserBookings() {
        List<BookingResponseDto> resultBookings = bookingService.getAll(booker.getId(), "ALL", PageRequest.of(0, 10));

        assertEquals(resultBookings.size(), 1);
        BookingResponseDto result = resultBookings.get(0);
        assertEquals(result.getStart(), booking.getStart());
        assertEquals(result.getEnd(), booking.getEnd());
        assertEquals(result.getStatus(), booking.getStatus());
        assertEquals(result.getItem().getId(), booking.getItem().getId());
        assertEquals(result.getBooker().getId(), booking.getBooker().getId());
    }
}