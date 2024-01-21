package com.example.characters;

import com.example.items.Item;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CharacterService {
    @Autowired
    private CharacterRepository repository;

    public List<Character> findAll() {
        return repository.findAll();
    }

    public Character create(Character character) {
         return repository.save(character);
    }

    public Optional<Character> findById(String id) {
        return repository.findById(id);
    }

    public Optional<Character> findOne(){
        return Optional.ofNullable(repository.findAll().get(0));
    }

}
