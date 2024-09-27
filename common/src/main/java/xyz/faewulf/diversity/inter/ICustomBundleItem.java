package xyz.faewulf.diversity.inter;

import net.minecraft.world.item.ItemStack;

public interface ICustomBundleItem {
    public void diversity_Multiloader$setMode(ItemStack itemStack, int value);

    public int diversity_Multiloader$getMode(ItemStack itemStack);
}
