package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemWithRequestResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestShortDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public ItemRequestShortDto addRequest(Long userId, ItemRequestShortDto requestDto) {
        User user = findUserIfExists(userId);
        ItemRequest itemRequest = ItemRequestMapper.itemRequestDtoToItemRequest(requestDto, user);
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest = itemRequestRepository.save(itemRequest);
        return ItemRequestMapper.itemRequestToItemRequestShortDto(itemRequest);
    }

    @Override
    public List<ItemRequestDto> getUserItemRequests(Long userId) {
        findUserIfExists(userId);
        List<ItemRequest> itemRequests = itemRequestRepository.findByRequestor_Id(userId);
        if (itemRequests.isEmpty())
            return Collections.emptyList();

        return getItemRequestsDtoWithItems(itemRequests);
    }

    @Override
    public List<ItemRequestDto> getOtherItemRequests(Long userId, PageRequest page) {
        findUserIfExists(userId);
        return getItemRequestsDtoWithItems(itemRequestRepository.findByRequestor_IdNot(userId, page));
    }

    @Override
    public ItemRequestDto getItemRequest(Long userId, Long itemRequestId) {
        findUserIfExists(userId);
        ItemRequest itemRequest = itemRequestRepository.findById(itemRequestId)
                .orElseThrow(() -> new NotFoundException("Запрос с id = " + itemRequestId + " не найден."));
        List<Item> itemsForRequest = itemRepository.findByRequest_Id(itemRequestId);
        return ItemRequestMapper.itemRequestToItemRequestResponseDto(itemRequest, ItemMapper.toItemShortDtoList(itemsForRequest));
    }

    private List<ItemRequestDto> getItemRequestsDtoWithItems(List<ItemRequest> itemRequests) {
        List<Long> requestsIds = itemRequests
                .stream()
                .map(ItemRequest::getId)
                .collect(toList());
        List<Item> itemsForRequests = itemRepository.findByRequest_IdIn(requestsIds);
        Map<ItemRequest, List<Item>> itemsMap = itemsForRequests
                .stream()
                .collect(groupingBy(Item::getRequest, toList()));
        List<ItemRequestDto> foundRequests = new ArrayList<>();
        for (ItemRequest itemRequest : itemRequests) {
            List<ItemWithRequestResponseDto> itemsForRequest = ItemMapper.toItemShortDtoList(itemsMap.get(itemRequest));
            foundRequests.add(ItemRequestMapper.itemRequestToItemRequestResponseDto(itemRequest, itemsForRequest));
        }
        return foundRequests;
    }

    private User findUserIfExists(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with  id = " + userId + " not found."));
    }
}
