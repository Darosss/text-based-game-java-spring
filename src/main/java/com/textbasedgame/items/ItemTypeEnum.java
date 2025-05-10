package com.textbasedgame.items;

public enum ItemTypeEnum {
    WEAPON_MELEE("Weapon Melee",
            new ItemsSubtypes[]{ItemsSubtypes.SWORD, ItemsSubtypes.MACE, ItemsSubtypes.DAGGER,
                    ItemsSubtypes.CLUB, ItemsSubtypes.SPEAR, ItemsSubtypes.WHIP,  ItemsSubtypes.SCIMITAR,
                    ItemsSubtypes.HATCHET
            },
            new MerchantOptions(1,3)
    ),
    WEAPON_MELEE_TWO_HAND("Weapon melee two hand",
            new ItemsSubtypes[]{ItemsSubtypes.SWORD_TWO_HAND ,ItemsSubtypes.AXE, ItemsSubtypes.KATANA,
                    ItemsSubtypes.HALBERD, ItemsSubtypes.SLEDGEHAMMER
            },
            new MerchantOptions(1,2)
    ),
    WEAPON_RANGED("Weapon Ranged",
            new ItemsSubtypes[]{ItemsSubtypes.BOW, ItemsSubtypes.CROSSBOW,
                    ItemsSubtypes.RANGED_HATCHET, ItemsSubtypes.RANGED_DAGGER, ItemsSubtypes.SLINGSHOT},
            new MerchantOptions(1,3)
    ),
    SHIELD("Shield",
            new ItemsSubtypes[]{ItemsSubtypes.BUCKLER, ItemsSubtypes.HEATER,
                    ItemsSubtypes.KITE, ItemsSubtypes.PAVISE, ItemsSubtypes.RONDACHE, ItemsSubtypes.TARGE,
                    ItemsSubtypes.WOODEN_SHIELD
            },
            new MerchantOptions(2,4)),
    CHEST_ARMOR("Chest Armor",
            new ItemsSubtypes[]{
                ItemsSubtypes.GAMBESON, ItemsSubtypes.SHELL, ItemsSubtypes.SCALE, ItemsSubtypes.LAMELLAR,
                    ItemsSubtypes.LAMINAR, ItemsSubtypes.PLATED_MAIL, ItemsSubtypes.CHAINMAIL, ItemsSubtypes.BRIGANDINE,
                    ItemsSubtypes.PLATE, ItemsSubtypes.LEATHER_ARMOR,
            },
            new MerchantOptions(2,5)),
    HELMET("Helmet",
            new ItemsSubtypes[]{
                ItemsSubtypes.KETTLE, ItemsSubtypes.SPANGENHELM, ItemsSubtypes.GREAT_HELM, ItemsSubtypes.HOUNSKULL, ItemsSubtypes.ARMET,
                ItemsSubtypes.LEATHER_HELMET, ItemsSubtypes.HAT
            },
            new MerchantOptions(2,5)),
    GLOVES("Gloves",
            new ItemsSubtypes[]{
                    ItemsSubtypes.GAUNTLET, ItemsSubtypes.LEATHER_GLOVES, ItemsSubtypes.IRON_GLOVES, ItemsSubtypes.STEEL_GLOVES
            },
            new MerchantOptions(2,5)),
    RING("Ring",
            new ItemsSubtypes[]{
                    ItemsSubtypes.SIMPLE_RING, ItemsSubtypes.SIGNET
            },
            new MerchantOptions(2,6)),
    NECKLACE("Necklace",
            new ItemsSubtypes[]{
                    ItemsSubtypes.CHAIN_NECKLACE, ItemsSubtypes.TORQUE, ItemsSubtypes.PEARLS_NECKLACE, ItemsSubtypes.LOCKET
            },
            new MerchantOptions(2,6)),
    BOOTS("Boots",
            new ItemsSubtypes[]{
                    ItemsSubtypes.LEATHER_BOOTS, ItemsSubtypes.STEEL_BOOTS, ItemsSubtypes.IRON_BOOTS
            },
            new MerchantOptions(2,6)),
    LEG_ARMOR("Leg Armor",
            new ItemsSubtypes[]{
                    ItemsSubtypes.LEATHER_LEG_ARMOR, ItemsSubtypes.STEEL_LEG_ARMOR, ItemsSubtypes.IRON_LEG_ARMOR
            },
            new MerchantOptions(2,6)),
    CONSUMABLE("Consumable",
            new ItemsSubtypes[]{ItemsSubtypes.COMMON_FOOD, ItemsSubtypes.HEALTH_POTION
            },
            new MerchantOptions(5,10)
            ),
    NEUTRAL("Neutral",
            new ItemsSubtypes[]{ItemsSubtypes.COMMON_NEUTRAL},
            new MerchantOptions(0, 0)),

    MERCENARY("Mercenary",
            new ItemsSubtypes[]{ItemsSubtypes.ASSASSIN, ItemsSubtypes.BATTLE_MASTER, ItemsSubtypes.MEDIC,
                    ItemsSubtypes.STRATEGIST, ItemsSubtypes.BARD, ItemsSubtypes.DEFENDER, ItemsSubtypes.SCOUT,
                    ItemsSubtypes.THUG
            }, new MerchantOptions(2, 5));

    private final String displayName;
    private final ItemsSubtypes[] subtypes;
    private final MerchantOptions merchantOptions;


    ItemTypeEnum(String displayName, ItemsSubtypes[] subtypes, MerchantOptions merchantOptions) {
        this.displayName = displayName;
        this.subtypes = subtypes;
        this.merchantOptions = merchantOptions;
    }

    public record MerchantOptions(int minItems, int maxItems){}

    public String getDisplayName() {
        return displayName;
    }

    public ItemsSubtypes[] getSubtypes() {
        return subtypes;
    }

    public MerchantOptions getMerchantOptions() {
        return merchantOptions;
    }
}
