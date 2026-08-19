package xyz.faewulf.diversity.mixin.core.increaseStackSize;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = net.minecraft.world.item.ItemStack.class, remap = false)
public class ItemStackMixin {
    @ModifyExpressionValue(method = {"lambda$static$1", "method_57371"}, at = @At(value = "CONSTANT", args = "intValue=99"))
    private static int sizeValueModifier(int original) {
        return 1024;
    }

    @Inject(method = "validatedStreamCodec", at = @At("HEAD"), cancellable = true)
    private static void validatedStreamCodec$skipExtraTripValidation(StreamCodec<RegistryFriendlyByteBuf, ItemStack> packetCodec, CallbackInfoReturnable<StreamCodec<RegistryFriendlyByteBuf, ItemStack>> cir) {
        cir.setReturnValue(packetCodec);
    }
}
