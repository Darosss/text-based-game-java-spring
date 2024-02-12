package com.example.characters;

import com.example.characters.equipment.CharacterEquipment;
import com.example.characters.equipment.EquipmentService;
import com.example.statistics.AdditionalStatisticsNamesEnum;
import com.example.statistics.BaseStatisticsNamesEnum;
import com.example.users.User;
import dev.morphia.Datastore;
import dev.morphia.DeleteOptions;
import dev.morphia.query.filters.Filters;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CharacterService {
    private Datastore datastore;
    private EquipmentService equipmentService;

    @Autowired
    public CharacterService(Datastore datastore, EquipmentService equipmentService) {
        this.datastore = datastore;
        this.equipmentService = equipmentService;
    }

    //TODO: make update better later, for now w/e
    public Character update(Character character) {
        return datastore.save(character);
    }

    public List<Character> findAll() {
        return datastore.find(Character.class).stream().toList();
    }

    public List<Character> findUserCharacters(String userId) {
        return datastore.find(Character.class).filter(Filters.eq("user", new ObjectId(userId))).stream().toList();
    }

    public Character create(User user, boolean asMainCharacter) {
         CharacterEquipment equipment = this.equipmentService.createForNewCharacter();

        Character character = new Character("Default character name", user, equipment, asMainCharacter);
        Character savedCharacter = datastore.save(character);
        equipment.setCharacter(savedCharacter);
        this.equipmentService.update(equipment);

        return savedCharacter;

    }
    public Character createDebugCharacter(
            User user, int level,long experience, boolean asMainCharacter,
            Map<BaseStatisticsNamesEnum, Integer> baseStatistic,
            Map<AdditionalStatisticsNamesEnum, Integer> additionalStats
            ) {

        CharacterEquipment equipment = this.equipmentService.createForNewCharacter();

        Character character = new Character("User character debug", user, equipment,
                level, experience, asMainCharacter, baseStatistic, additionalStats
                );

        Character savedCharacter = datastore.save(character);

        equipment.setCharacter(savedCharacter);
        this.equipmentService.update(equipment);

        return savedCharacter;
    }

    public Optional<Character> findById(String id) {
        return Optional.ofNullable(datastore.find(Character.class).filter(Filters.eq("id", new ObjectId(id))).first());
    }
    public Optional<Character> findOneByUserId(String userId) {
        return Optional.ofNullable(datastore.find(Character.class).filter(Filters.eq("user", new ObjectId(userId))).first());
    }
    public Optional<Character> findOneMainCharacterByUserId(String userId) {
        return Optional.ofNullable(datastore.find(Character.class).filter(
                Filters.eq("user", new ObjectId(userId)),
                Filters.eq("isMainCharacter", true)
        ).first());
    }

    public Character findOne(){
        return datastore.find(Character.class).first();
    }


    public void removeAllCharactersAndEquipments(){
        datastore
                .find(Character.class)
                .delete(new DeleteOptions().multi(true));

        datastore
                .find(CharacterEquipment.class)
                .delete(new DeleteOptions().multi(true));
    }
}
