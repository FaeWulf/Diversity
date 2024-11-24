package xyz.faewulf.diversity.EnchantHandler;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import xyz.faewulf.diversity.util.CustomEnchantCategory;

public class CapacityEnchantment extends Enchantment {

    private final CustomEnchantCategory category = CustomEnchantCategory.BUNDLE;

    public CapacityEnchantment() {
        super(Rarity.COMMON, CustomEnchantmentCategory.BUNDLE, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public int getMinCost(int $$0) {
        return 5 + ($$0 - 1) * 8;
    }

    @Override
    public int getMaxCost(int $$0) {
        return 20 + ($$0 - 1) * 10;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public boolean canEnchant(ItemStack $$0) {
        return $$0.getItem() instanceof BundleItem;
    }

    @Override
    public boolean isDiscoverable() {
        return true;
    }

}
