package it.sevenbits.sample.springboot.core.repository;

import it.sevenbits.sample.springboot.core.model.Item;

import java.util.List;

/**
 * Sample repository of items.
 */
public interface ItemsRepository {

    List<Item> getAllItems();

    Item create(Item newItem);

    Item getItemById(long id);

}
