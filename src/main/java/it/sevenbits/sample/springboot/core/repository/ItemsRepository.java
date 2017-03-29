package it.sevenbits.sample.springboot.core.repository;

import it.sevenbits.sample.springboot.core.model.Item;
import org.springframework.data.repository.CrudRepository;

/**
 * Sample repository of items.
 */
public interface ItemsRepository extends CrudRepository<Item, Long> {
}
