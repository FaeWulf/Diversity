package xyz.faewulf.diversity.platform;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import xyz.faewulf.diversity.Constants;
import xyz.faewulf.diversity.enchant.*;
import xyz.faewulf.diversity.util.CustomEnchant;
import xyz.faewulf.diversity.util.config.ModConfigs;

public class RegisterEnchantment {
    //pseudo
    public static void register() {

        if (ModConfigs.bundle_enchantment) {
            CustomEnchant.CAPACITY = Registry.register(BuiltInRegistries.ENCHANTMENT, new ResourceLocation(Constants.MOD_ID, "capacity"), new CapacityEnchantment());
            CustomEnchant.REFILL = Registry.register(BuiltInRegistries.ENCHANTMENT, new ResourceLocation(Constants.MOD_ID, "refill"), new RefillEnchantment());
        }
    }
}
