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

    public SampleItemsRepository() {
        //...
        items.add(new Item(0, "zero"));
        items.add(new Item(1, "one"));
        items.add(new Item(2, "two"));
    }

    @Override
    public List<Item> getAllItems() {
        return Collections.unmodifiableList(items);
    }

}
