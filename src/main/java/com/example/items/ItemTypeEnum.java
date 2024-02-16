package com.example.items;

public enum ItemTypeEnum {
    WEAPON_MELEE("Weapon Melee",
            new ItemsSubtypes[]{ItemsSubtypes.SWORD, ItemsSubtypes.MACE, ItemsSubtypes.DAGGER,
                    ItemsSubtypes.CLUB, ItemsSubtypes.SPEAR, ItemsSubtypes.WHIP,  ItemsSubtypes.SCIMITAR,
                    ItemsSubtypes.HATCHET
            }
    ),
    WEAPON_MELEE_TWO_HAND("Weapon melee two hand",
            new ItemsSubtypes[]{ItemsSubtypes.SWORD_TWO_HAND ,ItemsSubtypes.AXE, ItemsSubtypes.KATANA,
                    ItemsSubtypes.HALBERD, ItemsSubtypes.SLEDGEHAMMER
            }
    ),
    WEAPON_RANGED("Weapon Ranged",
            new ItemsSubtypes[]{ItemsSubtypes.BOW, ItemsSubtypes.CROSSBOW,
                    ItemsSubtypes.RANGED_HATCHET, ItemsSubtypes.RANGED_DAGGER, ItemsSubtypes.SLINGSHOT}),
    SHIELD("Shield",
            new ItemsSubtypes[]{ItemsSubtypes.BUCKLER, ItemsSubtypes.HEATER,
                    ItemsSubtypes.KITE, ItemsSubtypes.PAVISE, ItemsSubtypes.RONDACHE, ItemsSubtypes.TARGE,
                    ItemsSubtypes.WOODEN_SHIELD

    }),
    CHEST_ARMOR("Chest Armor",
            new ItemsSubtypes[]{
                ItemsSubtypes.GAMBESON, ItemsSubtypes.SHELL, ItemsSubtypes.SCALE, ItemsSubtypes.LAMELLAR,
                    ItemsSubtypes.LAMINAR, ItemsSubtypes.PLATED_MAIL, ItemsSubtypes.CHAINMAIL, ItemsSubtypes.BRIGANDINE,
                    ItemsSubtypes.PLATE, ItemsSubtypes.LEATHER_ARMOR,
    }),
    HELMET("Helmet",
            new ItemsSubtypes[]{
                ItemsSubtypes.KETTLE, ItemsSubtypes.SPANGENHELM, ItemsSubtypes.GREAT_HELM, ItemsSubtypes.HOUNSKULL, ItemsSubtypes.ARMET,
                ItemsSubtypes.LEATHER_HELMET, ItemsSubtypes.HAT
    }),
    GLOVES("Gloves",
            new ItemsSubtypes[]{
                    ItemsSubtypes.GAUNTLET, ItemsSubtypes.LEATHER_GLOVES, ItemsSubtypes.IRON_GLOVES, ItemsSubtypes.STEEL_GLOVES
            }),
    RING("Ring",
            new ItemsSubtypes[]{
                    ItemsSubtypes.SIMPLE_RING, ItemsSubtypes.SIGNET
            }),
    NECKLACE("Necklace",
            new ItemsSubtypes[]{
                    ItemsSubtypes.CHAIN_NECKLACE, ItemsSubtypes.TORQUE, ItemsSubtypes.PEARLS_NECKLACE, ItemsSubtypes.LOCKET}),
    BOOTS("Boots",
            new ItemsSubtypes[]{
                    ItemsSubtypes.LEATHER_BOOTS, ItemsSubtypes.STEEL_BOOTS, ItemsSubtypes.IRON_BOOTS}),
    LEG_ARMOR("Leg Armor",
            new ItemsSubtypes[]{
                    ItemsSubtypes.LEATHER_LEG_ARMOR, ItemsSubtypes.STEEL_LEG_ARMOR, ItemsSubtypes.IRON_LEG_ARMOR}),
    CONSUMABLE("Consumable",
            new ItemsSubtypes[]{ItemsSubtypes.COMMON_FOOD, ItemsSubtypes.HEALTH_POTION}),
    NEUTRAL("Neutral",
            new ItemsSubtypes[]{ItemsSubtypes.COMMON_NEUTRAL}),

    MERCENARY("Mercenary",
            new ItemsSubtypes[]{ItemsSubtypes.ASSASSIN, ItemsSubtypes.BATTLE_MASTER, ItemsSubtypes.MEDIC,
                    ItemsSubtypes.STRATEGIST, ItemsSubtypes.BARD, ItemsSubtypes.DEFENDER, ItemsSubtypes.SCOUT,
                    ItemsSubtypes.THUG
            });

    private final String displayName;
    private final ItemsSubtypes[] subtypes;

    ItemTypeEnum(String displayName, ItemsSubtypes[] subtypes) {
        this.displayName = displayName;
        this.subtypes = subtypes;
    }

    public String getDisplayName() {
        return displayName;
    }

    public ItemsSubtypes[] getSubtypes() {
        return subtypes;
    }
}
