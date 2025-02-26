package xyz.faewulf.diversity.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.trading.MerchantOffer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class CustomEnchant {

    public static final EquipmentSlot[] ARMOR_SLOTS = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};

    public static Enchantment CAPACITY;
    public static Enchantment REFILL;
    public static Enchantment VACUUM;
    public static Enchantment SELECTIVE_VACUUM;

    public static Enchantment BACKUP_PROTECTION;
    public static Enchantment BACKUP_FIRE_PROTECTION;
    public static Enchantment BACKUP_BLAST_PROTECTION;
    public static Enchantment BACKUP_PROJECTILE_PROTECTION;

    public static class EnchantBundleForEmeralds implements VillagerTrades.ItemListing {
        private final int villagerXp;

        public EnchantBundleForEmeralds(int i) {
            this.villagerXp = i;
        }

        @Override
        public MerchantOffer getOffer(Entity entity, RandomSource randomSource) {

            List<Enchantment> list = new ArrayList<>() {{
                if (CAPACITY != null) add(CAPACITY);
                if (REFILL != null) add(REFILL);
                if (VACUUM != null) add(VACUUM);
                if (SELECTIVE_VACUUM != null) add(SELECTIVE_VACUUM);
            }};

            Enchantment enchantment = (Enchantment) list.get(randomSource.nextInt(list.size()));
            int i = Mth.nextInt(randomSource, enchantment.getMinLevel(), enchantment.getMaxLevel());
            ItemStack itemStack = EnchantedBookItem.createForEnchantment(new EnchantmentInstance(enchantment, i));
            int j = 2 + randomSource.nextInt(5 + i * 10) + 3 * i;
            if (enchantment.isTreasureOnly()) {
                j *= 2;
            }

            if (j > 64) {
                j = 64;
            }

            return new MerchantOffer(new ItemStack(Items.EMERALD, j), new ItemStack(Items.BOOK), itemStack, 12, this.villagerXp, 0.2F);
        }
    }
}
