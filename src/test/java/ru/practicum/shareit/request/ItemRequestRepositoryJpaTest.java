package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class ItemRequestRepositoryJpaTest {
    PageRequest page = PageRequest.of(0, 3);
    @Autowired
    private TestEntityManager em;
    @Autowired
    private ItemRequestRepository itemRequestRepository;
    private User requestor;

    @BeforeEach
    void saveData() {
        requestor = new User();
        requestor.setName("testUserName");
        requestor.setEmail("testUserEmail@mail.com");
        em.persist(requestor);

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription("testItemDescription");
        itemRequest.setRequestor(requestor);
        itemRequest.setCreated(LocalDateTime.now());
        em.persist(itemRequest);
        em.flush();
    }

    @Test
    void findByRequestorId_PositiveTest() {
        List<ItemRequest> resultRequests = itemRequestRepository.findByRequestor_Id(requestor.getId());

        assertEquals(1, resultRequests.size());
    }

    @Test
    void findByRequestor_IdNot_Should_Return_Nothing_Test() {
        List<ItemRequest> resultRequests = itemRequestRepository.findByRequestor_IdNot(requestor.getId(), page);

        assertEquals(0, resultRequests.size());
    }

    @Test
    void findByRequestor_IdNot_Should_FoundOne_Test() {
        User user = new User();
        user.setName("testSecondUserName");
        user.setEmail("testSecondUserEmail@mail.ru");
        em.persist(user);

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription("testSecondItemDescription");
        itemRequest.setRequestor(user);
        itemRequest.setCreated(LocalDateTime.now());
        em.persist(itemRequest);
        em.flush();

        List<ItemRequest> resultRequests = itemRequestRepository.findByRequestor_IdNot(requestor.getId(), page);

        assertEquals(1, resultRequests.size());
    }
}