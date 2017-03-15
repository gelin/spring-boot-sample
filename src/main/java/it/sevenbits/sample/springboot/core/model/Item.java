package it.sevenbits.sample.springboot.core.model;

/**
 * A simple Item class.
 */
public class Item {

    private final long id;
    private final String name;

    public Item(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
