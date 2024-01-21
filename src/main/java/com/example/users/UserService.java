package com.example.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    @Autowired
    private ObjectMapper mapper;

    public List<User> findAll() {
        return repository.findAll();
    }

    public User create(User user) {
         return repository.save(user);
    }

}
