package xyz.faewulf.diversity.mixin.farmlandTrample;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.faewulf.diversity.util.ModConfigs;
import xyz.faewulf.diversity.util.converter;

@Mixin(FarmBlock.class)
public class FarmlandMixin {

    @Inject(method = "turnToDirt", at = @At("HEAD"), cancellable = true)
    private static void setToDirtMixin(Entity entity, BlockState state, Level world, BlockPos pos, CallbackInfo ci) {

        if (!ModConfigs.prevent_farmland_trampling)
            return;

        if (entity instanceof LivingEntity livingEntity) {
            if (EnchantmentHelper.getEnchantmentLevel(converter.getEnchant(world, Enchantments.FEATHER_FALLING), livingEntity) > 0 || livingEntity.hasEffect(MobEffects.SLOW_FALLING)) {
                ci.cancel();
            }
        }
    }

}
