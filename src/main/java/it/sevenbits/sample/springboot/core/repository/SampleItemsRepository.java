package it.sevenbits.sample.springboot.core.repository;

import it.sevenbits.sample.springboot.core.model.Item;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Keeps items in memory.
 */
@Repository
public class SampleItemsRepository implements ItemsRepository {

    private final Map<Long, Item> items = new HashMap<>();
    private int nextIndex = 0;

    public SampleItemsRepository() {
        add(new Item("one"));
        add(new Item("two"));
        add(new Item("three"));
    }

    private Item add(Item newItem) {
        Item createdItem = new Item(++nextIndex, newItem.getName());
        items.put(createdItem.getItemId(), createdItem);
        return createdItem;
    }

    @Override
    public List<Item> getAllItems() {
        List<Item> result = new ArrayList<>(items.size());
        result.addAll(items.values());
        return Collections.unmodifiableList(result);
    }

    @Override
    public Item create(Item newItem) {
        return add(newItem);
    }

    @Override
    public Item getItemById(long id) {
        return items.get(id);
    }

    @Override
    public Item update(long id, Item newItem) {
        Item existingItem = items.get(id);
        if (existingItem == null) {
            return null;
        } else {
            Item updatedItem = new Item(id, newItem.getName());
            items.put(id, updatedItem);
            return updatedItem;
        }
    }

    @Override
    public boolean delete(long id) {
        Item existingItem = items.get(id);
        if (existingItem == null) {
            return false;
        } else {
            items.remove(id);
            return true;
        }
    }
}
