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
    private final Datastore datastore;
    private final EquipmentService equipmentService;

    @Autowired
    public CharacterService(Datastore datastore, EquipmentService equipmentService) {
        this.datastore = datastore;
        this.equipmentService = equipmentService;
    }

    //TODO: make update better later, for now w/e
    public Character update(Character character) {
        return datastore.save(character);
    }

    public List<Character> findAll(){
        return this.datastore.find(Character.class).stream().toList();
    }
    public List<MercenaryCharacter> findUserMercenaries(ObjectId userId) {
        return datastore.find(MercenaryCharacter.class).filter(Filters.eq("user", userId)).stream().toList();
    }
    public List<MercenaryCharacter> findUserMercenaries(String userId) {
        return this.findUserMercenaries(new ObjectId(userId));
    }

    public MainCharacter createMainCharacter(User user, String name) {
        CharacterEquipment equipment = this.equipmentService.createForNewCharacter();

        MainCharacter savedCharacter = datastore.save(new MainCharacter(name, user, equipment, true));
        equipment.setCharacter(savedCharacter);
        this.equipmentService.update(equipment);

        return savedCharacter;
    }
    public MercenaryCharacter createMercenaryCharacter(User user, String name) {
        CharacterEquipment equipment = this.equipmentService.createForNewCharacter();

        MercenaryCharacter savedCharacter = datastore.save(new MercenaryCharacter(name, user, equipment, true));
        equipment.setCharacter(savedCharacter);
        this.equipmentService.update(equipment);

        return savedCharacter;
    }

    public MainCharacter createDebugCharacter(
            User user, int level, Map<BaseStatisticsNamesEnum, Integer> baseStatistic,
            Map<AdditionalStatisticsNamesEnum, Integer> additionalStats
            ) {

        CharacterEquipment equipment = this.equipmentService.createForNewCharacter();

        MainCharacter character = new MainCharacter("User character debug", user, equipment,
                level, baseStatistic, additionalStats
                );

        MainCharacter savedCharacter = datastore.save(character);

        equipment.setCharacter(savedCharacter);
        this.equipmentService.update(equipment);

        return savedCharacter;
    }

    public Optional<Character> findById(String id) {
        return Optional.ofNullable(datastore.find(Character.class)
                .filter(
                        Filters.eq("id", new ObjectId(id))).first());
    }
    public Optional<Character> findOneByUserId(String userId) {
        return Optional.ofNullable(datastore.find(Character.class).filter(Filters.eq("user", new ObjectId(userId))).first());
    }
    public Character findOne(){
        return datastore.find(Character.class).first();
    }

    public Optional<MainCharacter> findMainCharacterByUserId(ObjectId userId){
        return Optional.ofNullable(datastore.find(MainCharacter.class).filter(
                Filters.eq("user", userId)
        ).first());
    }
    public Optional<MainCharacter> findMainCharacterByUserId(String userId){
        return findMainCharacterByUserId(new ObjectId(userId));
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
