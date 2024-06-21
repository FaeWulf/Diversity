package faewulf.diversity.util;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class converter {
    public static RegistryEntry<Enchantment> getEnchant(World world, RegistryKey<Enchantment> enchant) {
        RegistryEntryLookup<Enchantment> registryEntryLookup = world.getRegistryManager()
                .createRegistryLookup()
                .getOrThrow(RegistryKeys.ENCHANTMENT);

        return registryEntryLookup.getOrThrow(enchant);
    }

    public static RegistryEntry<Enchantment> getEnchant(World world, String namespace, String path) {
        return world.getRegistryManager().get(RegistryKeys.ENCHANTMENT)
                .getEntry(Identifier.of(namespace, path))
                .orElseThrow(() -> new IllegalArgumentException("Enchantment with id " + path + " not found"));

    }
}
