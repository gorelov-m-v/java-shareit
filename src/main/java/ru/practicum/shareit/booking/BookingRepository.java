package ru.practicum.shareit.booking;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query(value = "SELECT b " +
            "FROM Booking as b " +
            "WHERE b.item.id = ?1 " +
            "ORDER BY b.start asc")
    List<Booking> findBookingsByItemAsc(Long itemId);

    List<Booking> findAllByBooker(User booker, Sort sort);

    List<Booking> findAllByBookerAndStartBeforeAndEndAfter(User booker, LocalDateTime start, LocalDateTime end, Sort sort);

    List<Booking> findByItem_IdAndBooker_IdAndEndBefore(Long userId, Long itemId, LocalDateTime end);

    List<Booking> findAllByBookerAndEndBefore(User booker, LocalDateTime end, Sort sort);

    List<Booking> findAllByBookerAndStartAfter(User booker, LocalDateTime start, Sort sort);

    List<Booking> findAllByBookerAndStatusEquals(User booker, BookingStatus status, Sort sort);

    List<Booking> findAllByItemOwner(User owner, Sort sort);

    List<Booking> findAllByItemOwnerAndStartBeforeAndEndAfter(User owner, LocalDateTime start, LocalDateTime end);

    List<Booking> findAllByItemOwnerAndEndBefore(User owner, LocalDateTime end, Sort sort);

    List<Booking> findAllByItemOwnerAndStartAfter(User owner, LocalDateTime start, Sort sort);

    List<Booking> findAllByItemOwnerAndStatusEquals(User owner, BookingStatus status, Sort sort);
}
