package xyz.faewulf.diversity.platform;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import xyz.faewulf.diversity.Constants;
import xyz.faewulf.diversity.enchant.*;
import xyz.faewulf.diversity.util.CustomEnchant;

public class RegisterEnchantment {
    //pseudo
    public static void register() {

        CustomEnchant.CAPACITY = Registry.register(BuiltInRegistries.ENCHANTMENT, new ResourceLocation(Constants.MOD_ID, "capacity"), new CapacityEnchantment());
        CustomEnchant.REFILL = Registry.register(BuiltInRegistries.ENCHANTMENT, new ResourceLocation(Constants.MOD_ID, "refill"), new RefillEnchantment());
        CustomEnchant.BACKUP_PROTECTION = Registry.register(BuiltInRegistries.ENCHANTMENT, new ResourceLocation(Constants.MOD_ID, "backup_protection"), new BackupProtectionEnchantment());
        CustomEnchant.BACKUP_FIRE_PROTECTION = Registry.register(BuiltInRegistries.ENCHANTMENT, new ResourceLocation(Constants.MOD_ID, "backup_fire_protection"), new BackupFireProtectionEnchantment());
        CustomEnchant.BACKUP_BLAST_PROTECTION = Registry.register(BuiltInRegistries.ENCHANTMENT, new ResourceLocation(Constants.MOD_ID, "backup_blast_protection"), new BackupBlastProtectionEnchantment());
        CustomEnchant.BACKUP_PROJECTILE_PROTECTION = Registry.register(BuiltInRegistries.ENCHANTMENT, new ResourceLocation(Constants.MOD_ID, "backup_projectile_protection"), new BackupProjectileProtectionEnchantment());

        return;
    }
}
