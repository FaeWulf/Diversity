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

    /*
    private static ResourceKey<LootTable> register(String id) {
        return registerLootTable(ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, id)));
    }
     */

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
}
