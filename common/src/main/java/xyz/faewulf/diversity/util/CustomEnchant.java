package xyz.faewulf.diversity.util;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;

public abstract class CustomEnchant {

    public static final EquipmentSlot[] ARMOR_SLOTS = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};

    public static Enchantment CAPACITY;
    public static Enchantment REFILL;
    public static Enchantment BACKUP_PROTECTION;
    public static Enchantment BACKUP_FIRE_PROTECTION;
    public static Enchantment BACKUP_BLAST_PROTECTION;
    public static Enchantment BACKUP_PROJECTILE_PROTECTION;
}
