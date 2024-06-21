package faewulf.diversity.inter;

import net.minecraft.item.ItemStack;

public interface ICustomBundleItem {
    public void setMode(ItemStack itemStack, int value);

    public int getMode(ItemStack itemStack);
}
