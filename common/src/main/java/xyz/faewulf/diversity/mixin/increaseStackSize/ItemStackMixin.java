package xyz.faewulf.diversity.mixin.increaseStackSize;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = net.minecraft.world.item.ItemStack.class, remap = false)
public class ItemStackMixin {
    @ModifyExpressionValue(method = {"lambda$static$3", "method_57371"}, at = @At(value = "CONSTANT", args = "intValue=99"))
    private static int sizeValueModifier(int original) {
        return 1024;
    }
}
