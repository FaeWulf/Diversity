package xyz.faewulf.diversity.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import xyz.faewulf.diversity.Constants;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class CustomLootTables {

    private static final Set<ResourceKey<LootTable>> LOOT_TABLES = new HashSet<>();
    private static final Set<ResourceKey<LootTable>> LOOT_TABLES_READ_ONLY = Collections.unmodifiableSet(LOOT_TABLES);

    public static final ResourceKey<LootTable> FOX_BURY = register("entity/fox_bury_behavior");
    public static final ResourceKey<LootTable> SNIFFER_MOSS_BLOCK = register("entity/sniffer_moss");

    private static ResourceKey<LootTable> register(String id) {
        return registerLootTable(ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, id)));
    }

    private static ResourceKey<LootTable> registerLootTable(ResourceKey<LootTable> key) {
        if (LOOT_TABLES.add(key)) {
            return key;
        } else {
            throw new IllegalArgumentException(key.location() + " is already a registered built-in loot table");
        }
    }

    public static Set<ResourceKey<LootTable>> getAll() {
        return LOOT_TABLES_READ_ONLY;
    }
}
