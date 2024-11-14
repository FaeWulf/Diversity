package xyz.faewulf.diversity.util;

import net.minecraft.resources.ResourceLocation;
import xyz.faewulf.diversity.Constants;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class CustomLootTables {

    private static final Set<ResourceLocation> LOOT_TABLES = new HashSet<>();
    private static final Set<ResourceLocation> LOOT_TABLES_READ_ONLY = Collections.unmodifiableSet(LOOT_TABLES);

    public static final ResourceLocation FOX_BURY = register("entity/fox_bury_behavior");
    public static final ResourceLocation SNIFFER_MOSS_BLOCK = register("entity/sniffer_moss");

    public static final ResourceLocation PICKPOCKET_ARMORER = register("villager/armorer");
    public static final ResourceLocation PICKPOCKET_BUTCHER = register("villager/butcher");
    public static final ResourceLocation PICKPOCKET_CARTOGRAPHER = register("villager/cartographer");
    public static final ResourceLocation PICKPOCKET_CLERIC = register("villager/cleric");
    public static final ResourceLocation PICKPOCKET_FARMER = register("villager/farmer");
    public static final ResourceLocation PICKPOCKET_FISHERMAN = register("villager/fisherman");
    public static final ResourceLocation PICKPOCKET_FLETCHER = register("villager/fletcher");
    public static final ResourceLocation PICKPOCKET_LEATHERWORKER = register("villager/leatherworker");
    public static final ResourceLocation PICKPOCKET_LIBRARIAN = register("villager/librarian");
    public static final ResourceLocation PICKPOCKET_MASON = register("villager/mason");
    public static final ResourceLocation PICKPOCKET_NITWIT = register("villager/nitwit");
    public static final ResourceLocation PICKPOCKET_NONE = register("villager/none");
    public static final ResourceLocation PICKPOCKET_SHEPHERD = register("villager/shepherd");
    public static final ResourceLocation PICKPOCKET_TOOLSMITH = register("villager/toolsmith");
    public static final ResourceLocation PICKPOCKET_WEAPONSMITH = register("villager/weaponsmith");


    private static ResourceLocation register(String $$0) {
        return registerLootTable(new ResourceLocation(Constants.MOD_ID, $$0));
    }

    private static ResourceLocation registerLootTable(ResourceLocation $$0) {
        if (LOOT_TABLES.add($$0)) {
            return $$0;
        } else {
            throw new IllegalArgumentException($$0 + " is already a registered built-in loot table");
        }
    }

    public static Set<ResourceLocation> getAll() {
        return LOOT_TABLES_READ_ONLY;
    }

    public static void init() {
    }
}
