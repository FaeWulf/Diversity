package xyz.faewulf.diversity.compat.EasyShulkerBoxes;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.apache.commons.lang3.math.Fraction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import xyz.faewulf.diversity.Constants;
import xyz.faewulf.diversity.util.config.ModConfigs;
import xyz.faewulf.lib.util.EnchantHelper;
import xyz.faewulf.lib.util.mixinPlugin.ConditionalMixin;

@Pseudo
@ConditionalMixin(configClass = ModConfigs.class, fieldName = "easy_shulker_box_compat")
@Mixin(targets = "fuzs.iteminteractions.api.v1.provider.impl.BundleProvider")
public class BundleProviderMixin {

    @ModifyExpressionValue(method = "createTooltipImageComponent", at = @At(value = "INVOKE", target = "Lfuzs/iteminteractions/api/v1/provider/impl/BundleProvider;getCapacityMultiplier()Lorg/apache/commons/lang3/math/Fraction;"))
    private Fraction modifyGetCapacityFraction(Fraction original, @Local(argsOnly = true) ItemStack containerStack, @Local(argsOnly = true) Player player) {

        if (!ModConfigs.easy_shulker_box_compat)
            return original;

        ItemEnchantments itemEnchantmentsComponent = EnchantmentHelper.getEnchantmentsForCrafting(containerStack);

        Holder<Enchantment> enchant = EnchantHelper.getEnchant(player.level(), Constants.MOD_ID, "capacity");
        if (enchant == null) return original;

        int value = itemEnchantmentsComponent.getLevel(enchant);

        Fraction candidate = Fraction.getFraction(value + 1, 1);

        // return the larger of the two fractions
        return candidate.compareTo(original) > 0 ? candidate : original;
    }

    @ModifyExpressionValue(method = "getMaxAmountToAdd", at = @At(value = "INVOKE", target = "Lfuzs/iteminteractions/api/v1/provider/impl/BundleProvider;getCapacityMultiplier()Lorg/apache/commons/lang3/math/Fraction;"))
    private Fraction modifyGetCapacityFraction2(Fraction original, @Local(argsOnly = true, ordinal = 0) ItemStack containerStack, @Local(argsOnly = true) Player player) {

        if (!ModConfigs.easy_shulker_box_compat)
            return original;

        ItemEnchantments itemEnchantmentsComponent = EnchantmentHelper.getEnchantmentsForCrafting(containerStack);

        Holder<Enchantment> enchant = EnchantHelper.getEnchant(player.level(), Constants.MOD_ID, "capacity");
        if (enchant == null) return original;

        int value = itemEnchantmentsComponent.getLevel(enchant);

        Fraction candidate = Fraction.getFraction(value + 1, 1);

        // return the larger of the two fractions
        return candidate.compareTo(original) > 0 ? candidate : original;
    }
}
