package it.sevenbits.sample.springboot.core.repository;

import it.sevenbits.sample.springboot.core.model.Item;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Keeps items in memory.
 */
@Repository
public class SampleItemsRepository implements ItemsRepository {

    private final List<Item> items = new ArrayList<>();
    private int nextIndex = 0;

    public SampleItemsRepository() {
        add(new Item("one"));
        add(new Item("two"));
        add(new Item("three"));
    }

    private Item add(Item newItem) {
        Item createdItem = new Item(++nextIndex, newItem.getName());
        items.add(createdItem);
        return createdItem;
    }

    @Override
    public List<Item> getAllItems() {
        return Collections.unmodifiableList(items);
    }

    @Override
    public Item create(Item newItem) {
        return add(newItem);
    }

}
