package com.example.skirmishes;

import com.example.characters.Character;
import com.example.users.User;
import dev.morphia.Datastore;
import dev.morphia.DeleteOptions;
import dev.morphia.query.filters.Filters;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SkirmishesService {
    private final Datastore datastore;


    @Autowired
    public SkirmishesService(Datastore datastore) {
        this.datastore = datastore;
    }

    public Character update(Character character) {
        return datastore.save(character);
    }
    public Optional<Skirmish> findOneByUserId(ObjectId userId) {
        return Optional.ofNullable(datastore.find(Skirmish.class).filter(Filters.eq("user", userId)).first());
    }
    public Optional<Skirmish> findOneByUserId(String userId) {
        return this.findOneByUserId(new ObjectId(userId));
    }
    public Skirmish create(User user, int challengesIteration) {
        Skirmish skirmish = new Skirmish(user, challengesIteration );
        return datastore.save(skirmish);
    }

    public Skirmish getOrCreateSkirmish(User user, int challengesIteration){
        Optional<Skirmish> foundSkirmish = this.findOneByUserId(user.getId());
        return foundSkirmish.orElseGet(() -> this.create(user, challengesIteration));
    }

    public Skirmish generateNewChallengesForUser(User user, int challengesIteration){
        Optional<Skirmish> foundSkirmish = this.findOneByUserId(user.getId());
        if(foundSkirmish.isPresent()) {
            Skirmish skirmishInstance = foundSkirmish.get();
            skirmishInstance.generateChallenges(challengesIteration);
            return this.update(skirmishInstance);
        };
        return create(user, challengesIteration);
    }

    public Skirmish update(Skirmish skirmish) {
        return datastore.save(skirmish);
    }

    public void removeById(ObjectId id){
        datastore
                .find(Skirmish.class)
                .filter(Filters.eq("id", id))
                .delete(new DeleteOptions().multi(true));

    }
}
