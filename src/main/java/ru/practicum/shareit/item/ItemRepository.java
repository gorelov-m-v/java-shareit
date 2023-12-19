package ru.practicum.shareit.item;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByOwnerId(Long userId, PageRequest page);

    @Query("SELECT item " +
            "FROM Item as item " +
            "WHERE item.available = true " +
            "AND (LOWER(item.name) LIKE LOWER(CONCAT('%', ?1,'%')) " +
            "OR LOWER(item.description) LIKE LOWER(CONCAT('%', ?1,'%')))")
    List<Item> findItemsByText(String text);

    List<Item> findByRequest_IdIn(List<Long> requestsIds);

    List<Item> findByRequest_Id(Long itemRequestId);
}