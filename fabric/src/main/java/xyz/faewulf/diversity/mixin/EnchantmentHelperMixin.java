package xyz.faewulf.diversity.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import xyz.faewulf.diversity.inter.ICustomEnchantCategory;
import xyz.faewulf.diversity.util.config.ModConfigs;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
    @ModifyExpressionValue(method = {"getAvailableEnchantmentResults"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentCategory;canEnchant(Lnet/minecraft/world/item/Item;)Z"))
    private static boolean changeValueInject(boolean original, @Local Enchantment enchantment, @Local Item item) {

        if (ModConfigs.more_enchantment)
            if (enchantment instanceof ICustomEnchantCategory iCustomEnchantCategory) {
                return iCustomEnchantCategory.getCategory().canEnchant(item);
            }
        return original;
    }
}
