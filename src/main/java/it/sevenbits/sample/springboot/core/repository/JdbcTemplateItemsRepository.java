package it.sevenbits.sample.springboot.core.repository;

import it.sevenbits.sample.springboot.core.model.Item;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * Takes items from database.
 */
public class JdbcTemplateItemsRepository implements ItemsRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateItemsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Item> getAllItems() {
        return jdbcTemplate.query(
                "SELECT id, name FROM item",
                (resultSet, i) -> {
                    long id = resultSet.getLong(1);
                    String name = resultSet.getString(2);
                    return new Item(id, name);
                });
    }

    @Override
    public Item create(Item newItem) {
        long id = getNextId();      // or generate UUID
        String name = newItem.getName();
        int rows = jdbcTemplate.update(
                "INSERT INTO item (id, name) VALUES (?, ?)",
                id, name
        );
        return new Item(id, name);  // or select from DB
    }

    private long getNextId() {
        return jdbcTemplate.queryForObject("select nextval('item_id_seq')", Long.class);
    }

    @Override
    public Item getItemById(long id) {
        return jdbcTemplate.queryForObject(
                "SELECT id, name FROM item WHERE id = ?",
                (resultSet, i) -> {
                    long rowId = resultSet.getLong(1);
                    String rowName = resultSet.getString(2);
                    return new Item(rowId, rowName);
                },
                id);
    }

    @Override
    public Item update(long id, Item newItem) {
        String name = newItem.getName();
        int rows = jdbcTemplate.update(
                "UPDATE item SET name = ? WHERE id = ?",
                name, id);
        return new Item(id, name);  // or select from DB
    }

    @Override
    public boolean delete(long id) {
        int rows = jdbcTemplate.update(
                "DELETE FROM item WHERE id = ?",
                id
        );
        return rows > 0;
    }

}
