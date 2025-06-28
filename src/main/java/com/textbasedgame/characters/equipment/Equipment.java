package com.textbasedgame.characters.equipment;

import com.textbasedgame.items.Item;

import java.util.Optional;

public interface Equipment {
    record EquipItemResult(boolean success, String message) {}
    record UnEquipItemResult(boolean success, String message, Optional<Item> item) {}
    record UseConsumableItemResult(boolean success, String message, Optional<Integer> newHealth) {}
}
