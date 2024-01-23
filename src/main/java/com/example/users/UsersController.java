package com.example.users;

import dev.morphia.query.Query;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class UsersController {

    private final UserService service;
    @Autowired()
    UsersController(UserService service) {
        this.service = service;
    }

    @GetMapping("/users")
    public List<User> findAll() {
        return this.service.findAll();
    }

    @PostMapping("/users")
    public User addModelTest(@RequestBody CreateUserDTO update){
        return service.create(new User(update));
    }

    @GetMapping("users/{id}")
    public Optional<User> getUserById(@PathVariable String id){
        return service.findOneById(id);
    }
}
