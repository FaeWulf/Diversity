package xyz.faewulf.diversity.platform;

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import xyz.faewulf.diversity.Constants;
import xyz.faewulf.diversity.enchant.*;

public class RegisterEnchantment {
    // Create a DeferredRegister for Enchantments
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, Constants.MOD_ID);

    public static final RegistryObject<Enchantment> CAPACITY = ENCHANTMENTS.register("capacity", CapacityEnchantment::new);
    public static final RegistryObject<Enchantment> REFILL = ENCHANTMENTS.register("refill", RefillEnchantment::new);
    public static final RegistryObject<Enchantment> BACKUP_PROTECTION = ENCHANTMENTS.register("backup_protection", BackupProtectionEnchantment::new);
    public static final RegistryObject<Enchantment> BACKUP_FIRE_PROTECTION = ENCHANTMENTS.register("backup_fire_protection", BackupFireProtectionEnchantment::new);
    public static final RegistryObject<Enchantment> BACKUP_BLAST_PROTECTION = ENCHANTMENTS.register("backup_blast_protection", BackupBlastProtectionEnchantment::new);
    public static final RegistryObject<Enchantment> BACKUP_PROJECTILE_PROTECTION = ENCHANTMENTS.register("backup_projectile_protection", BackupProjectileProtectionEnchantment::new);
}
