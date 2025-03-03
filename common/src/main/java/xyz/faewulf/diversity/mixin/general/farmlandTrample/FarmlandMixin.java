package xyz.faewulf.diversity.mixin.general.farmlandTrample;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.faewulf.diversity.util.config.ModConfigs;
import xyz.faewulf.lib.util.Converter;
import xyz.faewulf.lib.util.EnchantHelper;

@Mixin(FarmBlock.class)
public class FarmlandMixin {

    @Inject(method = "turnToDirt", at = @At("HEAD"), cancellable = true)
    private static void setToDirtMixin(Entity entity, BlockState state, Level world, BlockPos pos, CallbackInfo ci) {

        if (!ModConfigs.prevent_farmland_trampling)
            return;

        if (entity instanceof LivingEntity livingEntity) {
            Holder<Enchantment> enchantmentHolder = EnchantHelper.getEnchant(world, Enchantments.FEATHER_FALLING);
            if ((
                    enchantmentHolder != null && EnchantmentHelper.getEnchantmentLevel(enchantmentHolder, livingEntity) > 0
            )
                    || livingEntity.hasEffect(MobEffects.SLOW_FALLING)) {
                ci.cancel();
            }
        }
    }

}
