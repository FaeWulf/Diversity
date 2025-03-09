package xyz.faewulf.diversity_better_bundle.platform;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import xyz.faewulf.diversity_better_bundle.Constants;
import xyz.faewulf.diversity_better_bundle.enchant.CapacityEnchantment;
import xyz.faewulf.diversity_better_bundle.enchant.RefillEnchantment;
import xyz.faewulf.diversity_better_bundle.enchant.SelectiveVacuumEnchantment;
import xyz.faewulf.diversity_better_bundle.enchant.VacuumEnchantment;
import xyz.faewulf.diversity_better_bundle.util.CustomEnchant;
import xyz.faewulf.diversity_better_bundle.util.config.ModConfigs;

public class RegisterEnchantment {
    //pseudo
    public static void register() {

        if (ModConfigs.bundle_enchantment) {
            CustomEnchant.CAPACITY = Registry.register(BuiltInRegistries.ENCHANTMENT, new ResourceLocation(Constants.MOD_ID, "capacity"), new CapacityEnchantment());
            CustomEnchant.REFILL = Registry.register(BuiltInRegistries.ENCHANTMENT, new ResourceLocation(Constants.MOD_ID, "refill"), new RefillEnchantment());
            CustomEnchant.VACUUM = Registry.register(BuiltInRegistries.ENCHANTMENT, new ResourceLocation(Constants.MOD_ID, "vacuum"), new VacuumEnchantment());
            CustomEnchant.SELECTIVE_VACUUM = Registry.register(BuiltInRegistries.ENCHANTMENT, new ResourceLocation(Constants.MOD_ID, "selective_vacuum"), new SelectiveVacuumEnchantment());
        }
    }
}
