package com.example.users;

import com.mongodb.MongoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;


    public List<User> findAll() {
        return repository.findAll();
    }

    public User create(User user) {
         return repository.save(user);
    }

    public Optional<User> findOneById(String id) {
        return repository.findById(id);
    }

}
