package com.example.characters.equipment;

import com.example.characters.Character;
import com.example.items.Item;
import com.example.items.ItemTypeEnum;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.EnumSet;

@Document(collection = "equipments")
    public class CharacterEquipment {
    @Id
    private String id;
    @DBRef
    private Character character;
    @DBRef
    private Item head = null;
    @DBRef
    private Item leftHand = null;
    @DBRef
        private Item rightHand = null;
    public CharacterEquipment() {}


    private boolean isCorrectItemToWear(EnumSet<ItemTypeEnum> requiredTypes, ItemTypeEnum itemType) {
        return requiredTypes.contains(itemType);
    }

    private boolean canWearOnHead(Item item) {
        EnumSet<ItemTypeEnum> requiredTypes = EnumSet.of(ItemTypeEnum.HELMET);
        return this.head == null && isCorrectItemToWear(requiredTypes, item.getType());
    }
    private boolean canWearOnLHand(Item item) {
        EnumSet<ItemTypeEnum> requiredTypes = EnumSet.of(ItemTypeEnum.WEAPON_MELEE, ItemTypeEnum.WEAPON_RANGED);
        return (this.leftHand == null) &&
                isCorrectItemToWear(requiredTypes, item.getType());
    }
    private boolean canWearOnRHand(Item item) {
        EnumSet<ItemTypeEnum> requiredTypes = EnumSet.of(ItemTypeEnum.WEAPON_MELEE, ItemTypeEnum.WEAPON_RANGED);
        return (this.rightHand == null) &&
                isCorrectItemToWear(requiredTypes, item.getType());
    }

        public boolean equipItem(CharacterEquipmentFieldsEnum slot, Item item) {
            return switch (slot) {
                case HEAD -> {
                    if (canWearOnHead(item)) {
                        this.head = item;
                        yield true;
                    }
                    yield false;
                }
                case LEFT_HAND -> {
                    if (canWearOnLHand(item)) {
                        this.leftHand = item;
                        yield true;
                    }
                    yield false;
                }

                case RIGHT_HAND -> {
                    if (canWearOnRHand(item)) {
                        this.rightHand = item;
                        yield true;
                    }
                    yield false;
                }
                default -> false;
            };
        }

        public Item unequipItem(CharacterEquipmentFieldsEnum slot){
             return switch (slot) {
                case HEAD: {
                    if(this.head != null) {
                        Item unequippedItem = this.head;
                        this.head = null;
                        yield unequippedItem;
                    }

                }
                case LEFT_HAND: {
                    if(this.leftHand != null) {
                        Item unequippedItem = this.leftHand;
                        this.leftHand = null;
                        yield unequippedItem;
                    }
                }

                case RIGHT_HAND: {
                    if(this.rightHand != null) {
                        Item unequippedItem = this.rightHand;
                        this.rightHand = null;
                        yield unequippedItem;
                    }
                }
                default:
                    yield  null;

            };

        }

        public Item getEquippedItemByField(CharacterEquipmentFieldsEnum slot) {
            return switch (slot) {
                case HEAD -> this.head;
                case LEFT_HAND -> this.leftHand;
                case RIGHT_HAND -> this.rightHand;
                default -> null;
            };
        }


        public String getId() {
            return id;
        }

        public Item getHead() {
            return head;
        }

        public Item getLeftHand() {
            return leftHand;
        }

        public Item getRightHand() {
            return rightHand;
        }
        public void setCharacter(Character character) {
            this.character = character;
        }


}
