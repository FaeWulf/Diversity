package xyz.faewulf.diversity.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.enchantment.Enchantment;
import xyz.faewulf.diversity.Constants;

public interface CustomEnchantmentTags {

    TagKey<Enchantment> LEATHER_WORKER_BOOK_TRADE = create("leatherworker");

    private static TagKey<Enchantment> create(String name) {
        return TagKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, name));
    }
}
