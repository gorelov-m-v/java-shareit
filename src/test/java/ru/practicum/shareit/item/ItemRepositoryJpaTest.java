package ru.practicum.shareit.item;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class ItemRepositoryJpaTest {
    static PageRequest page;
    @Autowired
    private TestEntityManager testEntityManager;
    @Autowired
    private ItemRepository itemRepository;
    private Item item;
    private User owner;

    @BeforeAll
    public static void setUp() {
        page = PageRequest.of(0, 1);
    }

    @BeforeEach
    void saveData() {
        owner = new User();
        owner.setName("TestUserName");
        owner.setEmail("testemail@mail.com");
        testEntityManager.persist(owner);

        item = new Item();
        item.setName("testItemName");
        item.setDescription("TestItemDescription");
        item.setOwner(owner);
        item.setAvailable(true);
        testEntityManager.persist(item);
        testEntityManager.flush();
    }

    @Test
    void findItemsByText_ByName_PositiveTest() {
        List<Item> resultItems = itemRepository.findItemsByText("Name");

        Item resultItem = resultItems.get(0);

        assertAll(
                () -> assertEquals(resultItems.size(), 1),
                () -> assertEquals(resultItem.getName(), item.getName()),
                () -> assertEquals(resultItem.getDescription(), item.getDescription()),
                () -> assertEquals(resultItem.getAvailable(), item.getAvailable()),
                () -> assertEquals(resultItem.getOwner(), item.getOwner()));
    }

    @Test
    void findItemsByText_ByDescription_PositiveTest() {
        List<Item> resultItems = itemRepository.findItemsByText("Description");

        Item resultItem = resultItems.get(0);

        assertAll(
                () -> assertEquals(resultItems.size(), 1),
                () -> assertEquals(resultItem.getName(), item.getName()),
                () -> assertEquals(resultItem.getDescription(), item.getDescription()),
                () -> assertEquals(resultItem.getAvailable(), item.getAvailable()),
                () -> assertEquals(resultItem.getOwner(), item.getOwner()));
    }

    @Test
    void findItemsByWrongText_ShouldReturn_NothingTest() {
        List<Item> resultItems = itemRepository.findItemsByText("Nothing");

        assertEquals(resultItems.size(), 0);
    }

    @Test
    void findByOwnerId_PositiveTest() {
        List<Item> resultItems = itemRepository.findByOwnerId(owner.getId(), page);

        assertEquals(1, resultItems.size());
    }

    @Test
    void findByItemRequestId_PositiveTest() {
        User requestor = new User();
        requestor.setName("secondTestName");
        requestor.setEmail("secondTest@mail.com");
        testEntityManager.persist(requestor);

        ItemRequest request = new ItemRequest();
        request.setCreated(LocalDateTime.now());
        request.setRequestor(requestor);
        request.setDescription("testRequestDescription");
        testEntityManager.persist(request);

        Item item = new Item();
        item.setName("secondTestItemName");
        item.setDescription("secondTestItemDescription");
        item.setOwner(owner);
        item.setAvailable(true);
        item.setRequest(request);
        testEntityManager.persist(item);
        testEntityManager.flush();

        List<Item> resultItems = itemRepository.findByRequest_Id(request.getId());

        assertEquals(1, resultItems.size());
    }

    @AfterEach
    void deleteData() {
        itemRepository.deleteAll();
    }
}
