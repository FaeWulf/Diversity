package xyz.faewulf.diversity.platform;

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import xyz.faewulf.diversity.Constants;
import xyz.faewulf.diversity.EnchantHandler.CapacityEnchantment;
import xyz.faewulf.diversity.EnchantHandler.RefillEnchantment;
import xyz.faewulf.diversity.enchant.BackupBlastProtectionEnchantment;
import xyz.faewulf.diversity.enchant.BackupFireProtectionEnchantment;
import xyz.faewulf.diversity.enchant.BackupProjectileProtectionEnchantment;
import xyz.faewulf.diversity.enchant.BackupProtectionEnchantment;
import xyz.faewulf.diversity.util.ModConfigs;

public class RegisterEnchantment {
    // Create a DeferredRegister for Enchantments
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, Constants.MOD_ID);

    public static RegistryObject<Enchantment> CAPACITY;
    public static RegistryObject<Enchantment> REFILL;
    public static RegistryObject<Enchantment> BACKUP_PROTECTION;
    public static RegistryObject<Enchantment> BACKUP_FIRE_PROTECTION;
    public static RegistryObject<Enchantment> BACKUP_BLAST_PROTECTION;
    public static RegistryObject<Enchantment> BACKUP_PROJECTILE_PROTECTION;

    public static void init() {

        if (ModConfigs.more_enchantment) {
            BACKUP_PROTECTION = ENCHANTMENTS.register("backup_protection", BackupProtectionEnchantment::new);
            BACKUP_FIRE_PROTECTION = ENCHANTMENTS.register("backup_fire_protection", BackupFireProtectionEnchantment::new);
            BACKUP_BLAST_PROTECTION = ENCHANTMENTS.register("backup_blast_protection", BackupBlastProtectionEnchantment::new);
            BACKUP_PROJECTILE_PROTECTION = ENCHANTMENTS.register("backup_projectile_protection", BackupProjectileProtectionEnchantment::new);
        }

        if (ModConfigs.bundle_enchantment) {
            CAPACITY = ENCHANTMENTS.register("capacity", CapacityEnchantment::new);
            REFILL = ENCHANTMENTS.register("refill", RefillEnchantment::new);
        }

    }
}
