package xyz.faewulf.diversity.compat.EasyShulkerBoxes;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.apache.commons.lang3.math.Fraction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import xyz.faewulf.diversity.Constants;
import xyz.faewulf.diversity.util.config.ModConfigs;
import xyz.faewulf.diversity.util.converter;
import xyz.faewulf.diversity.util.mixinPlugin.ConditionalMixin;

@Pseudo
@ConditionalMixin(configClass = ModConfigs.class, fieldName = "easy_shulker_box_compat")
@Mixin(targets = "fuzs.iteminteractions.api.v1.provider.impl.BundleProvider")
public class BundleProviderMixin {

    @ModifyExpressionValue(method = "createTooltipImageComponent", at = @At(value = "INVOKE", target = "Lfuzs/iteminteractions/api/v1/provider/impl/BundleProvider;getCapacityMultiplier(Lnet/minecraft/world/item/ItemStack;)Lorg/apache/commons/lang3/math/Fraction;"))
    private Fraction modifyGetCapacityFraction(Fraction original, @Local(argsOnly = true) ItemStack containerStack, @Local(argsOnly = true) Player player) {

        if (!ModConfigs.easy_shulker_box_compat)
            return original;

        ItemEnchantments itemEnchantmentsComponent = EnchantmentHelper.getEnchantmentsForCrafting(containerStack);
        int value = itemEnchantmentsComponent.getLevel(converter.getEnchant(player.level(), Constants.MOD_ID, "capacity"));

        return Fraction.getFraction(value + 1, 1);
    }

    @ModifyExpressionValue(method = "getMaxAmountToAdd", at = @At(value = "INVOKE", target = "Lfuzs/iteminteractions/api/v1/provider/impl/BundleProvider;getCapacityMultiplier(Lnet/minecraft/world/item/ItemStack;)Lorg/apache/commons/lang3/math/Fraction;"))
    private Fraction modifyGetCapacityFraction2(Fraction original, @Local(argsOnly = true, ordinal = 0) ItemStack containerStack, @Local(argsOnly = true) Player player) {

        if (!ModConfigs.easy_shulker_box_compat)
            return original;

        ItemEnchantments itemEnchantmentsComponent = EnchantmentHelper.getEnchantmentsForCrafting(containerStack);
        int value = itemEnchantmentsComponent.getLevel(converter.getEnchant(player.level(), Constants.MOD_ID, "capacity"));

        return Fraction.getFraction(value + 1, 1);
    }
}
