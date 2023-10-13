package ru.practicum.shareit.item.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByIdOwner(long idOwner);

    int countItemByIdOwner(Long idOwner);

    Item findByIdOwner(long idOwner);

    List<Item> findAllByDescriptionContainingIgnoreCaseAndAvailableTrue(String description);

    Optional<Item> findByIdAndIdOwner(long itemId,long ownerId);
}
