package it.sevenbits.sample.springboot.core.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.ResourceSupport;

/**
 * A simple Item class.
 */
public class Item extends ResourceSupport {

    private final long item_id;
    private final String name;

    @JsonCreator
    public Item(@JsonProperty("item_id") long id, @JsonProperty("name") String name) {
        this.item_id = id;
        this.name = name;
    }

    public Item(String name) {
        this(0, name);
    }

    @JsonProperty("item_id")
    public long getItemId() {
        return item_id;
    }

    public String getName() {
        return name;
    }

}
