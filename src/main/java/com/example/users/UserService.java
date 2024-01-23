package com.example.users;

import com.example.characters.equipment.CharacterEquipment;
import com.mongodb.MongoException;
import dev.morphia.Datastore;
import dev.morphia.query.Query;
import dev.morphia.query.filters.Filter;
import dev.morphia.query.filters.Filters;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UserService {
    private final Datastore datastore;


    @Autowired
    public UserService(Datastore datastore) {
        this.datastore = datastore;
    }

    public List<User> findAll() {
        return datastore.find(User.class).stream().toList();
    }

    public User create(User user) {
        return datastore.save(user);
    }

    public Optional<User> findOneById(String id) {
        return Optional.ofNullable(datastore.find(User.class)
                .filter(Filters.eq("id", new ObjectId(id))).first());
    }

    //TODO: make update better later, for now w/e
    public User update(User user) {
        return datastore.save(user);
    }
}
