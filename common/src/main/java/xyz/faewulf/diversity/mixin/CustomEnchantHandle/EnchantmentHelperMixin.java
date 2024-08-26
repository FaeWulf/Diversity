package xyz.faewulf.diversity.mixin.CustomEnchantHandle;

import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
    /*
    @ModifyExpressionValue(method = {"getAvailableEnchantmentResults"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentCategory;canEnchant(Lnet/minecraft/world/item/Item;)Z"))
    private static boolean changeValueInject(boolean original, @Local Enchantment enchantment, @Local Item item) {
        if (enchantment instanceof ICustomEnchantCategory iCustomEnchantCategory) {
            return iCustomEnchantCategory.getCategory().canEnchant(item);
        }
        return original;
    }
     */
}
