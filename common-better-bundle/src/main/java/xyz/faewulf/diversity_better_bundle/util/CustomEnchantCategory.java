package xyz.faewulf.diversity_better_bundle.util;

import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.Item;

public enum CustomEnchantCategory {

    BUNDLE {
        @Override
        public boolean canEnchant(Item $$0) {
            return $$0 instanceof BundleItem;
        }
    };

    public abstract boolean canEnchant(Item var1);
}
