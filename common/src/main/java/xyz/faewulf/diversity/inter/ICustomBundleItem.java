package xyz.faewulf.diversity.inter;

import net.minecraft.world.item.ItemStack;

public interface ICustomBundleItem {
    void diversity_Multiloader$setMode(ItemStack itemStack, int value);

    int diversity_Multiloader$getMode(ItemStack itemStack);
}
