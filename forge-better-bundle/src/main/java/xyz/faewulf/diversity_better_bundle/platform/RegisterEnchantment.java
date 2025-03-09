package xyz.faewulf.diversity_better_bundle.platform;

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import xyz.faewulf.diversity_better_bundle.Constants;
import xyz.faewulf.diversity_better_bundle.EnchantHandler.CapacityEnchantment;
import xyz.faewulf.diversity_better_bundle.EnchantHandler.RefillEnchantment;
import xyz.faewulf.diversity_better_bundle.EnchantHandler.SelectiveVacuumEnchantment;
import xyz.faewulf.diversity_better_bundle.EnchantHandler.VacuumEnchantment;
import xyz.faewulf.diversity_better_bundle.util.config.ModConfigs;

public class RegisterEnchantment {
    // Create a DeferredRegister for Enchantments
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, Constants.MOD_ID);

    public static RegistryObject<Enchantment> CAPACITY;
    public static RegistryObject<Enchantment> REFILL;
    public static RegistryObject<Enchantment> VACUUM;
    public static RegistryObject<Enchantment> SELECTIVE_VACUUM;

    public static void init() {

        if (ModConfigs.bundle_enchantment) {
            CAPACITY = ENCHANTMENTS.register("capacity", CapacityEnchantment::new);
            REFILL = ENCHANTMENTS.register("refill", RefillEnchantment::new);
            VACUUM = ENCHANTMENTS.register("vacuum", VacuumEnchantment::new);
            SELECTIVE_VACUUM = ENCHANTMENTS.register("selective_vacuum", SelectiveVacuumEnchantment::new);
        }

    }
}
