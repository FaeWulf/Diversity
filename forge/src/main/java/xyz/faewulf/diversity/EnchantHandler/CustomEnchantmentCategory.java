package xyz.faewulf.diversity.EnchantHandler;

import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class CustomEnchantmentCategory {
    public static final EnchantmentCategory BUNDLE = EnchantmentCategory.create("BUNDLE", item -> item instanceof BundleItem);
}