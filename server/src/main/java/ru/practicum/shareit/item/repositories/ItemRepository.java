package ru.practicum.shareit.item.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Page<Item> findAllByIdOwner(long idOwner, Pageable pageable);

    int countItemByIdOwner(Long idOwner);

    Item findByIdOwner(long idOwner);

    Page<Item> findAllByDescriptionContainingIgnoreCaseAndAvailableTrue(String description,Pageable pageable);

    Optional<Item> findByIdAndIdOwner(long itemId,long ownerId);

    List<Item> findAllByRequestId(long id);
}
