package xyz.faewulf.diversity.platform;

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import xyz.faewulf.diversity.Constants;
import xyz.faewulf.diversity.EnchantHandler.CapacityEnchantment;
import xyz.faewulf.diversity.EnchantHandler.RefillEnchantment;
import xyz.faewulf.diversity.util.config.ModConfigs;

public class RegisterEnchantment {
    // Create a DeferredRegister for Enchantments
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, Constants.MOD_ID);

    public static RegistryObject<Enchantment> CAPACITY;
    public static RegistryObject<Enchantment> REFILL;

    public static void init() {

        if (ModConfigs.bundle_enchantment) {
            CAPACITY = ENCHANTMENTS.register("capacity", CapacityEnchantment::new);
            REFILL = ENCHANTMENTS.register("refill", RefillEnchantment::new);
        }

    }
}
