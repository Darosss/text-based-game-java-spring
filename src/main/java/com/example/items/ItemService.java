package com.example.items;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;
@Service
public class ItemService {
    @Autowired
    private ItemRepository repository;


    public Optional<Item> findOne(String id) {
        return repository.findById(id);
    }
    public Item create(Item item) { return repository.save(item); };
}
