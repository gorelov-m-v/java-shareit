package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemWithRequestResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestShortDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemRequestServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private ItemRequestRepository itemRequestRepository;

    private ItemRequestService itemRequestService;

    @BeforeEach
    void initItemRequestService() {
        itemRequestService = new ItemRequestServiceImpl(itemRequestRepository, userRepository, itemRepository);
    }

    @Test
    void testAddRequest_WithWrongUser() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        NotFoundException result = assertThrows(NotFoundException.class,
                () -> itemRequestService.addRequest(1L, new ItemRequestShortDto(1L, "Description",
                        LocalDateTime.now())));
        assertEquals(result.getMessage(), "User with  id = 1 not found.");
    }

    @Test
    void testAddRequest() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(itemRequestRepository.save(any())).thenReturn(new ItemRequest());
        itemRequestService.addRequest(1L, new ItemRequestShortDto(null, "Description", null));
        verify(itemRequestRepository, times(1)).save(any());
    }

    @Test
    void testGetItemRequest_WithWrongId() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.empty());
        NotFoundException result = assertThrows(NotFoundException.class,
                () -> itemRequestService.getItemRequest(1L, 1L));
        assertEquals(result.getMessage(), "Запрос с id = 1 не найден.");
    }

    @Test
    void testGetItemRequest_WithoutItem() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));

        User requestor = new User();
        requestor.setId(1L);
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setRequestor(requestor);
        itemRequest.setDescription("Description");
        itemRequest.setCreated(LocalDateTime.now());
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.of(itemRequest));
        when(itemRepository.findByRequest_Id(anyLong())).thenReturn(new ArrayList<>());

        ItemRequestDto result = itemRequestService.getItemRequest(1L, 1L);
        assertEquals(result.getId(), itemRequest.getId());
        assertEquals(result.getDescription(), itemRequest.getDescription());
        assertEquals(result.getCreated(), itemRequest.getCreated());
        assertEquals(result.getItems().size(), 0);
    }

    @Test
    void testGetItemRequest_WithItem() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));

        User requestor = new User();
        requestor.setId(1L);
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setRequestor(requestor);
        itemRequest.setDescription("Description");
        itemRequest.setCreated(LocalDateTime.now());
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.of(itemRequest));

        Item item = new Item();
        item.setId(1L);
        item.setAvailable(true);
        item.setName("Шуруповерт");
        item.setDescription("Надежный, бесшумный, мощный");
        item.setRequest(itemRequest);
        item.setOwner(new User());
        when(itemRepository.findByRequest_Id(anyLong())).thenReturn(List.of(item));

        ItemRequestDto result = itemRequestService.getItemRequest(1L, 1L);
        assertEquals(result.getId(), itemRequest.getId());
        assertEquals(result.getDescription(), itemRequest.getDescription());
        assertEquals(result.getCreated(), itemRequest.getCreated());

        assertNotNull(result.getItems());
        assertEquals(result.getItems().size(), 1);
        ItemWithRequestResponseDto resultItem = result.getItems().get(0);
        assertEquals(resultItem.getId(), item.getId());
        assertEquals(resultItem.getName(), item.getName());
        assertEquals(resultItem.getDescription(), item.getDescription());
        assertEquals(resultItem.getAvailable(), item.getAvailable());
        assertEquals(resultItem.getRequestId(), item.getRequest().getId());
    }

    @Test
    void testGetOtherItemRequests() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setDescription("Ищу канистру");
        itemRequest.setCreated(LocalDateTime.of(2023, 12, 20, 12, 12));
        when(itemRequestRepository.findByRequestor_IdNot(anyLong(), any())).thenReturn(List.of(itemRequest));

        List<ItemRequestDto> resultRequests = itemRequestService.getOtherItemRequests(1L, PageRequest.of(1, 10));

        assertEquals(1, resultRequests.size());
        assertEquals(itemRequest.getCreated(), resultRequests.get(0).getCreated());
    }
}
