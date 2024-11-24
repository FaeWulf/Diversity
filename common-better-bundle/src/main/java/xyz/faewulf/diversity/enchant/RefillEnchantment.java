package xyz.faewulf.diversity.enchant;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import xyz.faewulf.diversity.inter.ICustomEnchantCategory;
import xyz.faewulf.diversity.util.CustomEnchantCategory;

public class RefillEnchantment extends Enchantment implements ICustomEnchantCategory {

    private final CustomEnchantCategory category = CustomEnchantCategory.BUNDLE;

    public RefillEnchantment() {
        super(Rarity.COMMON, EnchantmentCategory.VANISHABLE, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public int getMinCost(int $$0) {
        return 22;
    }

    @Override
    public int getMaxCost(int $$0) {
        return 30;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public boolean canEnchant(ItemStack $$0) {
        return $$0.getItem() instanceof BundleItem;
    }


    @Override
    public boolean isDiscoverable() {
        return true;
    }

    @Override
    public CustomEnchantCategory getCategory() {
        return this.category;
    }
}
