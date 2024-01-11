package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.item.dto.ItemWithCommentResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceImplIntegrationTest {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemService itemService;
    private Item item;
    private User owner;

    @BeforeEach
    void saveItems() {
        item = new Item();
        item.setId(1L);
        item.setName("testItemName");
        item.setDescription("testItemDescription");
        item.setAvailable(true);

        owner = new User();
        owner.setEmail("testMail@mail.ru");
        owner.setName("testUserName");
        item.setOwner(owner);
        owner = userRepository.save(owner);
        item = itemRepository.save(item);
    }

    @Test
    void testGetUserItems() {
        List<ItemWithCommentResponseDto> resultItems = itemService.getUserItems(owner.getId(), PageRequest.of(0, 3));

        ItemWithCommentResponseDto result = resultItems.get(0);

        assertAll(
                () -> assertEquals(1, resultItems.size()),
                () -> assertEquals(item.getName(), result.getName()),
                () -> assertEquals(item.getDescription(), result.getDescription()),
                () -> assertEquals(item.getAvailable(), result.getAvailable()));
    }
}
