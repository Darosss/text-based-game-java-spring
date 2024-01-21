package com.example.characters;

import com.example.items.*;
import com.example.statistics.BaseStatisticObject;
import com.example.statistics.BaseStatisticsNamesEnum;
import com.example.statistics.StatisticsNamesEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class CharactersController {


    @Autowired()
    private final CharacterService service = new CharacterService();

    @Autowired
    private final ItemService serv2 = new ItemService();

    @GetMapping("/characters")

    public Map<StatisticsNamesEnum, List<ItemStatisticsObject>> findAll() {
        List<ItemStatisticsObject> items = new ArrayList<>();
//        ItemStatisticsObject minDamageStat = new ItemStatisticsObject(StatisticsNamesEnum.MIN_DAMAGE,
//                20, ItemStatisticsObject.ValueType.ABSOLUTE);
//
//        ItemStatisticsObject maxDamageStat = new ItemStatisticsObject(StatisticsNamesEnum.MAX_DAMAGE,
//                20, ItemStatisticsObject.ValueType.ABSOLUTE);
//        items.add(minDamageStat);
//        items.add(maxDamageStat);
//        this.serv2.create(new Item("name", "desc",
//                12, 23000, ItemTypeEnum.GLOVES, ItemRarityEnum.RARE,
//                items));
        Optional<Item> item = this.serv2.findOne("65abf3dc53869121ddd3194a");

        if(item.isPresent()) {
           return item.get().getStatisticsByName();
        }
        return null;
        //        return this.service.findAll();
    }

    @PostMapping("/characters")
    public Character create(){
        return service.create(new Character());
    }

    @PostMapping("/characters-debug")
    public Character createDebug(){
        return service.create(new Character(50, 20L, 50));
    }



    @GetMapping("/characters/statistics/{name}/effective-value")
        public int getEffectiveValueByStatName(@PathVariable BaseStatisticsNamesEnum name) {
            Optional<Character> foundChar = service.findOne();

        return foundChar.map(character -> character.getBaseStatistics().
                get(name).getEffectiveValue()).orElse(0);

    }

    @GetMapping("/characters/statistics/{name}")
    public Optional<BaseStatisticObject> getCharacterStats(@PathVariable BaseStatisticsNamesEnum name) {
        Optional<Character> foundChar = service.findOne();

        return foundChar.map(character -> character.getBaseStatistics().get(name));


    }


}
