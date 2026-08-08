package xyz.faewulf.diversity.compat.MetalBundles;

import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.math.Fraction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Invoker;

@Pseudo
@Mixin(targets = "fuzs.metalbundles.world.item.MetalBundleItem")
public interface MetalBundleItemInvoker {
    @Invoker("getActualWeight")
    public static Fraction getActualWeightInvoker(ItemStack itemStack) {
        throw new AssertionError();
    }
}
