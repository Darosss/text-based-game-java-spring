package com.example.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class UsersController {

    private static final String template = "Hello, $s!";
    private final AtomicLong counter = new AtomicLong();

    @Autowired()
    private final UserService service = new UserService();

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
