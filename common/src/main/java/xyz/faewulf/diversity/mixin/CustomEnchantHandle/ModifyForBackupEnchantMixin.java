package xyz.faewulf.diversity.mixin.CustomEnchantHandle;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.apache.commons.lang3.mutable.MutableInt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.faewulf.diversity.enchant.BackupProtectionEnchantment;

@Mixin(EnchantmentHelper.class)
public abstract class ModifyForBackupEnchantMixin {

    @Shadow
    private static void runIterationOnInventory(EnchantmentHelper.EnchantmentVisitor p_44854_, Iterable<ItemStack> p_44855_) {
    }

    @Inject(method = "getDamageProtection", at = @At(value = "TAIL"), cancellable = true)
    private static void getDmgInject(Iterable<ItemStack> $$0, DamageSource $$1, CallbackInfoReturnable<Integer> cir, @Local MutableInt mutableInt) {
        MutableInt $$2 = new MutableInt();
        runIterationOnInventory(($$2x, $$3) -> {
            if ($$2x instanceof BackupProtectionEnchantment) {
                $$2.add($$2x.getDamageProtection($$3, $$1));
            } else {
                $$2.add(0);
            }
        }, $$0);

        mutableInt.subtract($$2.intValue());
        mutableInt.add(Math.ceil($$2.intValue() * 1.0f / 4));

        if ($$2.intValue() > 0) {
            cir.setReturnValue(mutableInt.intValue());
            cir.cancel();
        }
    }
}
