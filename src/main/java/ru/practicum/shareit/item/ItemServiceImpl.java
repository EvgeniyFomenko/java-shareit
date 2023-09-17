package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.AccessDenideException;
import ru.practicum.shareit.item.model.Item;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;

    @Override
    public List<Item> getAll(long userId) {
        return itemRepository.getAll().stream().filter(e -> Objects.equals(e.getIdOwner(), userId)).collect(Collectors.toList());
    }

    @Override
    public Item get(long id) {
        return itemRepository.get(id);
    }

    @Override
    public Item create(Item item, long userId) {
        item.setIdOwner(userId);

        return itemRepository.create(item);
    }

    @Override
    public Item update(Item item, long userId) {
        validation(userId, item.getId());

        Item item1 = itemRepository.get(item.getId());
        if (Objects.nonNull(item.getName())) {
            item1.setName(item.getName());
        }
        if (Objects.nonNull(item.getDescription())) {
            item1.setDescription(item.getDescription());
        }
        if (Objects.nonNull(item.getAvailable())) {
            item1.setAvailable(item.getAvailable());
        }

        return itemRepository.update(item1);
    }

    private void validation(long userId, long itemId) {
        if (userId != itemRepository.get(itemId).getIdOwner()) {
            throw new AccessDenideException("Пльзователю запрещено менять этот item");
        }
    }

    @Override
    public void delete(long id) {
        itemRepository.delete(id);
    }

    @Override
    public List<ItemDto> search(String text) {
        if (text.isBlank()) {
            return Collections.EMPTY_LIST;
        }
        return itemRepository.getAll().stream()
                .filter(item -> item.getName().toLowerCase()
                        .contains(text.toLowerCase()) || item.getDescription().toLowerCase().contains(text.toLowerCase())).filter(Item::getAvailable)
                .map(ItemDtoMapper::mapToDto).collect(Collectors.toList());
    }
}
