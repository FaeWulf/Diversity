package xyz.faewulf.diversity_better_bundle.mixin.general.bundleEnchantments;

import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.stream.Stream;

@Mixin(BundleItem.class)
public interface BundleItemInvoker {
    @Invoker("getContentWeight")
    public static int getContentWeightInvoked(ItemStack $$0) {
        throw new AssertionError();
    }

    @Invoker("getContents")
    public static Stream<ItemStack> getContentsInvoked(ItemStack $$0) {
        throw new AssertionError();
    }
}
