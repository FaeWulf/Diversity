package xyz.faewulf.diversity.mixin.fabric.compat;

import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.faewulf.diversity.util.config.ModConfigs;

@Pseudo
@Mixin(targets = "moriyashiine.enchancement.common.util.EnchancementUtil")
public class EnchancementCompat {

    @Inject(method = "getNonDefaultEnchantmentsSize", at = @At("RETURN"), cancellable = true)
    private static void exceedsLimitModifyReturnValue(ItemStack stack, int size, CallbackInfoReturnable<Integer> cir) {
        if (!ModConfigs.enchancement_compat)
            return;

        if (stack.getItem() instanceof BundleItem) {
            cir.setReturnValue(0);
            cir.cancel();
        }
    }
}
