package ru.practicum.shareit.booking;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class BookingRepositoryJpaTest {
    @Autowired
    private TestEntityManager em;
    @Autowired
    private BookingRepository bookingRepository;
    private User booker;
    private Item item;
    private PageRequest page = PageRequest.of(0, 3).withSort(Sort.Direction.DESC, "id");


    @BeforeEach
    void saveData() {
        booker = new User();
        booker.setName("testBookerName");
        booker.setEmail("testBookerEmail@mail.com");
        em.persist(booker);

        User owner = new User();
        owner.setName("testUserName");
        owner.setEmail("testUserEmail@yandex.ru");
        em.persist(owner);

        item = new Item();
        item.setName("testItemName");
        item.setDescription("TestItemDescription");
        item.setOwner(owner);
        item.setAvailable(true);
        em.persist(item);
        em.flush();
    }

    @Test
    void findByBookerIdAndStartBeforeAndEndAfter_PositiveTest() {
        Booking current = new Booking();
        current.setItem(item);
        current.setBooker(booker);
        current.setStatus(BookingStatus.APPROVED);
        current.setStart(LocalDateTime.now().minusDays(5));
        current.setEnd(LocalDateTime.now().plusDays(2));
        em.persist(current);
        em.flush();

        List<Booking> resultBookings = bookingRepository.findAllByBookerAndStartBeforeAndEndAfter(booker,
                LocalDateTime.now(), LocalDateTime.now(), page);
        assertEquals(1, resultBookings.size());
    }

    @Test
    void findByBooker_IdAndEndBefore_PositiveTest() {
        Booking pastBooking = new Booking();
        pastBooking.setItem(item);
        pastBooking.setBooker(booker);
        pastBooking.setStatus(BookingStatus.APPROVED);
        pastBooking.setStart(LocalDateTime.now().minusDays(5));
        pastBooking.setEnd(LocalDateTime.now().minusDays(2));
        em.persist(pastBooking);
        em.flush();

        List<Booking> resultBookings = bookingRepository.findAllByBookerAndEndBefore(booker,
                LocalDateTime.now(), page);
        assertEquals(1, resultBookings.size());
    }

    @Test
    void findByBooker_IdAndStartAfter_PostitiveTest() {
        Booking futureBooking = new Booking();
        futureBooking.setItem(item);
        futureBooking.setBooker(booker);
        futureBooking.setStatus(BookingStatus.APPROVED);
        futureBooking.setStart(LocalDateTime.now().plusDays(3));
        futureBooking.setEnd(LocalDateTime.now().plusDays(4));
        em.persist(futureBooking);
        em.flush();

        List<Booking> resultBookings = bookingRepository.findAllByBookerAndStartAfter(booker,
                LocalDateTime.now(), page);
        assertEquals(1, resultBookings.size());
    }

    @Test
    void findByBooker_IdAndStatus_PositiveTest() {
        Booking rejectedBooking = new Booking();
        rejectedBooking.setItem(item);
        rejectedBooking.setBooker(booker);
        rejectedBooking.setStatus(BookingStatus.REJECTED);
        rejectedBooking.setStart(LocalDateTime.now().plusDays(3));
        rejectedBooking.setEnd(LocalDateTime.now().plusDays(4));
        em.persist(rejectedBooking);
        em.flush();

        List<Booking> resultBookings = bookingRepository.findAllByBookerAndStatusEquals(booker,
                BookingStatus.REJECTED, page);
        assertEquals(1, resultBookings.size());
    }

    @Test
    void findByBooker_Id_PositiveTest() {
        Booking booking = new Booking();
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.WAITING);
        booking.setStart(LocalDateTime.now().plusDays(3));
        booking.setEnd(LocalDateTime.now().plusDays(4));
        em.persist(booking);
        em.flush();

        List<Booking> resultBookings = bookingRepository.findAllByBooker(booker, page);
        assertEquals(1, resultBookings.size());
    }

    @AfterEach
    void deleteData() {
        bookingRepository.deleteAll();
    }
}