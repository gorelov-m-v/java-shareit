package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.validator.Create;
import ru.practicum.shareit.validator.Update;

import java.util.List;

@RestController
@RequestMapping("/items")
@Validated
@Slf4j
public class ItemController {

    private ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ItemResponseDto addItem(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody @Validated({Create.class}) ItemRequestDto itemRequestDto) {
        log.debug("Добавление вещи {} пользователя с id = {}.", itemRequestDto.getName(), userId);
        ItemResponseDto savedItem = itemService.addItem(userId, itemRequestDto);
        log.debug("Вещь добавлена.");
        return savedItem;
    }

    @PatchMapping("/{itemId}")
    public ItemResponseDto updateItem(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable("itemId") Long itemId,
                              @RequestBody @Validated({Update.class}) ItemRequestDto item) {
        log.debug("Обновление вещи id = {} пользователя c id = {}.", itemId, userId);
        ItemResponseDto updatedItem = itemService.updateItem(userId, itemId, item);
        log.debug("Данные обновлены.");
        return updatedItem;
    }

    @GetMapping("/{itemId}")
    public ItemResponseDto getItem(@PathVariable("itemId") Long itemId) {
        log.debug("Поиск вещи id = {}", itemId);
        ItemResponseDto foundItem = itemService.getItem(itemId);
        log.debug("Найдена вещь: {}.", foundItem);
        return foundItem;
    }

    @GetMapping
    public List<ItemResponseDto> getUserItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.debug("Поиск всех вещей пользователя id = {}.", userId);
        List<ItemResponseDto> foundItems = itemService.getUserItems(userId);
        log.debug("Найдены вещи: {}.", foundItems);
        return foundItems;
    }

    @GetMapping("/search")
    public List<ItemResponseDto> searchItems(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam String text) {
        log.debug("Поиск вещей по запросу {}.", text);
        List<ItemResponseDto> foundItems = itemService.searchItems(userId, text);
        log.debug("Найдены вещи: {}.", foundItems);
        return foundItems;
    }
}
