package it.sevenbits.sample.springboot.web.controllers;

import it.sevenbits.sample.springboot.core.model.Item;
import it.sevenbits.sample.springboot.core.repository.ItemsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

/**
 * Provides operations for one item.
 */
@Controller
@RequestMapping("/item/{id}")
public class ItemController {

    private final ItemsRepository itemsRepository;

    @Autowired
    public ItemController(ItemsRepository itemsRepository) {
        this.itemsRepository = itemsRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Item> get(@PathVariable long id) {
        Item result = itemsRepository.getItemById(id);
        if (result == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(result);
        }
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<Item> update(@PathVariable long id, @RequestBody Item newItem) {
        Item result = itemsRepository.update(id, newItem);
        if (result == null) {
            return ResponseEntity.notFound().build();
        } else {
            URI location = UriComponentsBuilder.fromPath("/items/").path(String.valueOf(result.getId())).build().toUri();
            return ResponseEntity.created(location).body(result);
        }
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity delete(@PathVariable long id) {
        boolean deleted = itemsRepository.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
