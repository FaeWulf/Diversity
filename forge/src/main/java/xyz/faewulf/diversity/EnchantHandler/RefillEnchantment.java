package xyz.faewulf.diversity.EnchantHandler;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import xyz.faewulf.diversity.util.CustomEnchantCategory;

public class RefillEnchantment extends Enchantment {

    private final CustomEnchantCategory category = CustomEnchantCategory.BUNDLE;

    public RefillEnchantment() {
        super(Rarity.COMMON, CustomEnchantmentCategory.BUNDLE, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
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
}
