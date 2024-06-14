package faewulf.diversity.util;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;

public class converter {
    public static RegistryEntry<Enchantment> getEnchant(World world, RegistryKey<Enchantment> enchant) {
        RegistryEntryLookup<Enchantment> registryEntryLookup = world.getRegistryManager().createRegistryLookup().getOrThrow(RegistryKeys.ENCHANTMENT);
        return registryEntryLookup.getOrThrow(enchant);
    }
}
