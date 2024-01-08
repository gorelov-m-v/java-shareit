package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.item.dto.ItemWithCommentResponseDto;
import ru.practicum.shareit.user.dto.UserResponseDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTest {
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private BookingService bookingService;
    @Autowired
    private MockMvc mvc;
    private BookingResponseDto bookingDto;

    @BeforeEach
    public void createBookingDto() {
        bookingDto = new BookingResponseDto(1L, LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusDays(2), BookingStatus.WAITING);
        bookingDto.setItem(new ItemWithCommentResponseDto(
                10L, "testItemName", "testItemDescription", true));
        bookingDto.setBooker(new UserResponseDto(2L, "testUser", "test@email.com"));

        System.out.println(bookingDto);
    }

    @Test
    public void createBookingTest() throws Exception {
        when(bookingService.create(any(), anyLong()))
                .thenReturn(bookingDto);
        BookingRequestDto request = new BookingRequestDto(bookingDto.getStart(),
                bookingDto.getEnd(), bookingDto.getItem().getId());
        mvc.perform(MockMvcRequestBuilders.post("/bookings")
                        .header("X-Sharer-User-Id", bookingDto.getBooker().getId())
                        .content(mapper.writeValueAsString(request))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(BookingStatus.WAITING.name())))
                .andExpect(jsonPath("$.item.id", is(bookingDto.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.item.name", is(bookingDto.getItem().getName())))
                .andExpect(jsonPath("$.booker.id", is(bookingDto.getBooker().getId()), Long.class));
    }

    @Test
    public void updateBookingTest() throws Exception {
        bookingDto.setStatus(BookingStatus.APPROVED);
        when(bookingService.update(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(bookingDto);

        mvc.perform(MockMvcRequestBuilders.patch("/bookings/1?approved=true")
                        .header("X-Sharer-User-Id", bookingDto.getBooker().getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(BookingStatus.APPROVED.name())))
                .andExpect(jsonPath("$.item.id", is(bookingDto.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.item.name", is(bookingDto.getItem().getName())))
                .andExpect(jsonPath("$.booker.id", is(bookingDto.getBooker().getId()), Long.class));
    }

    @Test
    public void getBooking() throws Exception {
        when(bookingService.get(anyLong(), anyLong()))
                .thenReturn(bookingDto);

        mvc.perform(MockMvcRequestBuilders.get("/bookings/1")
                        .header("X-Sharer-User-Id", bookingDto.getBooker().getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(BookingStatus.WAITING.name())))
                .andExpect(jsonPath("$.item.id", is(bookingDto.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.item.name", is(bookingDto.getItem().getName())))
                .andExpect(jsonPath("$.booker.id", is(bookingDto.getBooker().getId()), Long.class));
    }

    @Test
    public void getUserBookingWithNoParameters() throws Exception {
        when(bookingService.getOwnerItemsAll(anyLong(), any(), any()))
                .thenReturn(List.of(bookingDto));

        mvc.perform(MockMvcRequestBuilders.get("/bookings/owner")
                        .header("X-Sharer-User-Id", bookingDto.getBooker().getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].status", is(BookingStatus.WAITING.name())))
                .andExpect(jsonPath("$[0].item.id", is(bookingDto.getItem().getId()), Long.class))
                .andExpect(jsonPath("$[0].item.name", is(bookingDto.getItem().getName())))
                .andExpect(jsonPath("$[0].booker.id", is(bookingDto.getBooker().getId()), Long.class));
    }

    @Test
    public void getOwnerItemsAllWithParameters() throws Exception {
        when(bookingService.getOwnerItemsAll(anyLong(), any(), any()))
                .thenReturn(List.of(bookingDto));

        mvc.perform(MockMvcRequestBuilders.get("/bookings/owner?state=CURRENT&from=0&size=2")
                        .header("X-Sharer-User-Id", bookingDto.getBooker().getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].status", is(BookingStatus.WAITING.name())))
                .andExpect(jsonPath("$[0].item.id", is(bookingDto.getItem().getId()), Long.class))
                .andExpect(jsonPath("$[0].item.name", is(bookingDto.getItem().getName())))
                .andExpect(jsonPath("$[0].booker.id", is(bookingDto.getBooker().getId()), Long.class));
    }

    @Test
    public void getOwnerItemsAllWithoutParameters() throws Exception {
        when(bookingService.getOwnerItemsAll(anyLong(), any(), any()))
                .thenReturn(List.of(bookingDto));

        mvc.perform(MockMvcRequestBuilders.get("/bookings/owner")
                        .header("X-Sharer-User-Id", bookingDto.getBooker().getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].status", is(BookingStatus.WAITING.name())))
                .andExpect(jsonPath("$[0].item.id", is(bookingDto.getItem().getId()), Long.class))
                .andExpect(jsonPath("$[0].item.name", is(bookingDto.getItem().getName())));

    }

    @Test
    public void getOwnerItemsAllWithParametersTest() throws Exception {
        when(bookingService.getOwnerItemsAll(anyLong(), any(), any()))
                .thenReturn(List.of(bookingDto));

        mvc.perform(MockMvcRequestBuilders.get("/bookings/owner?state=CURRENT&from=0&size=2")
                        .header("X-Sharer-User-Id", bookingDto.getBooker().getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].status", is(BookingStatus.WAITING.name())))
                .andExpect(jsonPath("$[0].item.id", is(bookingDto.getItem().getId()), Long.class))
                .andExpect(jsonPath("$[0].item.name", is(bookingDto.getItem().getName())));
    }
}