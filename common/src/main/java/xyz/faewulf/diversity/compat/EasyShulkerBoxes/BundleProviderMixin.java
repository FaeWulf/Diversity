package xyz.faewulf.diversity.compat.EasyShulkerBoxes;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import xyz.faewulf.diversity.util.CustomEnchant;
import xyz.faewulf.diversity.util.config.ModConfigs;
import xyz.faewulf.diversity.util.mixinPlugin.ConditionalMixin;

@Pseudo
@ConditionalMixin(configClass = ModConfigs.class, fieldName = "easy_shulker_box_compat")
@Mixin(targets = "fuzs.puzzlesapi.api.iteminteractions.v1.provider.BundleProvider")
public class BundleProviderMixin {

    @ModifyExpressionValue(method = "getAvailableBundleItemSpace", at = @At(value = "INVOKE", target = "Lfuzs/puzzlesapi/api/iteminteractions/v1/provider/BundleProvider;getCapacity()I"))
    private int modifyGetCapacity(int original, @Local(argsOnly = true, ordinal = 0) ItemStack containerStack, @Local(argsOnly = true) Player player) {

        if (!ModConfigs.easy_shulker_box_compat)
            return original;

        int value = EnchantmentHelper.getItemEnchantmentLevel(CustomEnchant.CAPACITY, containerStack);
        return 64 + value * 64;
    }

    @ModifyExpressionValue(method = "createTooltipImageComponent", at = @At(value = "INVOKE", target = "Lfuzs/puzzlesapi/api/iteminteractions/v1/provider/BundleProvider;getCapacity()I"))
    private int modifyGetCapacity2(int original, @Local(argsOnly = true) ItemStack containerStack, @Local(argsOnly = true) Player player) {

        if (!ModConfigs.easy_shulker_box_compat)
            return original;

        int value = EnchantmentHelper.getItemEnchantmentLevel(CustomEnchant.CAPACITY, containerStack);
        return 64 + value * 64;
    }
}
