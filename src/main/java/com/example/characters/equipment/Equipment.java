package com.example.characters.equipment;

import com.example.items.Item;

import java.util.Optional;

public interface Equipment {
    record EquipItemResult(boolean success, String message) {}
    record UnEquipItemResult(boolean success, String message, Optional<Item> item) {}
}
