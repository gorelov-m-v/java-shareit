package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    private final ItemRepository itemRepository;

    private final UserRepository userRepository;
    private final Sort sort = Sort.by(Sort.Direction.DESC, "start");


    @Override
    public BookingResponseDto create(BookingRequestDto bookingRequestDto, Long userId) throws NotFoundException {
        User user = findUserIfExists(userId);
        Item item = findItemIfExists(bookingRequestDto.getItemId());

        if (item.getOwner().getId().equals(userId))
            throw new NotFoundException("This is your item.");

        if (!item.getAvailable())
            throw new InvalidArgumentException("Item with id " + item.getId() + " is not available.");

        if (bookingRequestDto.getEnd().isBefore(bookingRequestDto.getStart()))
            throw new InvalidArgumentException("end can't be earlier than start.");

        if (bookingRequestDto.getEnd().equals(bookingRequestDto.getStart()))
            throw new InvalidArgumentException("end can't be equals start.");
        Booking booking = BookingMapper.bookingRequestDtoToBooking(bookingRequestDto);

        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);

        booking = bookingRepository.save(booking);

        return BookingMapper.bookingToBookingResponseDto(booking);
    }

    @Override
    public BookingResponseDto update(Long bookingId, Long userId, Boolean approved) throws NotFoundException {
        Booking booking = findBookingIfExists(bookingId);

        if (!booking.getItem().getOwner().getId().equals(userId))
            throw new NotFoundException("This is not your item.");

        if (!booking.getStatus().equals(BookingStatus.WAITING))
            throw new InvalidArgumentException("Only available for status " + BookingStatus.WAITING);

        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }

        bookingRepository.save(booking);

        return BookingMapper.bookingToBookingResponseDto(booking);
    }

    @Override
    public BookingResponseDto get(Long bookingId, Long userId) throws NotFoundException {
        Booking booking = findBookingIfExists(bookingId);

        if (!booking.getBooker().getId().equals(userId) && !booking.getItem().getOwner().getId().equals(userId))
            throw new NotFoundException("Only available to owner or booker");

        return BookingMapper.bookingToBookingResponseDto(booking);
    }

    @Override
    public List<BookingResponseDto> getAll(Long userId, String state) {
        User user = findUserIfExists(userId);
        List<Booking> bookingList = new ArrayList<>();

        switch (state) {
            case "ALL":
                bookingList.addAll(bookingRepository.findAllByBooker(user, sort));
                break;
            case "CURRENT":
                bookingList.addAll(bookingRepository.findAllByBookerAndStartBeforeAndEndAfter(user,
                        LocalDateTime.now(), LocalDateTime.now(), sort));
                break;
            case "PAST":
                bookingList.addAll(bookingRepository.findAllByBookerAndEndBefore(user,
                        LocalDateTime.now(), sort));
                break;
            case "FUTURE":
                bookingList.addAll(bookingRepository.findAllByBookerAndStartAfter(user, LocalDateTime.now(), sort));
                break;
            case "WAITING":
                bookingList.addAll(bookingRepository.findAllByBookerAndStatusEquals(user, BookingStatus.WAITING, sort));
                break;
            case "REJECTED":
                bookingList.addAll(bookingRepository.findAllByBookerAndStatusEquals(user, BookingStatus.REJECTED, sort));
                break;
            default:
                throw new InvalidArgumentException("Unknown state: " + state);
        }

        return bookingList.stream().map(BookingMapper::bookingToBookingResponseDto).collect(Collectors.toList());
    }

    @Override
    public List<BookingResponseDto> getOwnerItemsAll(Long userId, String state) throws NotFoundException {
        User user = findUserIfExists(userId);
        List<Booking> bookingList = new ArrayList<>();

        switch (state) {
            case "ALL":
                bookingList.addAll(bookingRepository.findAllByItemOwner(user, sort));
                break;
            case "CURRENT":
                bookingList.addAll(bookingRepository.findAllByItemOwnerAndStartBeforeAndEndAfter(user,
                        LocalDateTime.now(), LocalDateTime.now(), sort));
                break;
            case "PAST":
                bookingList.addAll(bookingRepository.findAllByItemOwnerAndEndBefore(user,
                        LocalDateTime.now(), sort));
                break;
            case "FUTURE":
                bookingList.addAll(bookingRepository.findAllByItemOwnerAndStartAfter(user, LocalDateTime.now(), sort));
                break;
            case "WAITING":
                bookingList.addAll(bookingRepository.findAllByItemOwnerAndStatusEquals(user, BookingStatus.WAITING, sort));
                break;
            case "REJECTED":
                bookingList.addAll(bookingRepository.findAllByItemOwnerAndStatusEquals(user, BookingStatus.REJECTED, sort));
                break;
            default:
                throw new InvalidArgumentException("Unknown state: " + state);
        }
        return bookingList.stream().map(BookingMapper::bookingToBookingResponseDto).collect(Collectors.toList());
    }

    private User findUserIfExists(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with  id = " + userId + " not found."));
    }

    private Item findItemIfExists(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item with  id = " + itemId + " not found."));
    }

    private Booking findBookingIfExists(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking with  id = " + bookingId + " not found."));
    }
}
