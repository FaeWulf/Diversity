package faewulf.diversity.mixin.increaseStackSize;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(net.minecraft.item.ItemStack.class)
public class ItemStackMixin {
    @ModifyExpressionValue(method = "method_57371", at = @At(value = "CONSTANT", args = "intValue=99"))
    private static int temp(int original) {
        return 1024;
    }

}
