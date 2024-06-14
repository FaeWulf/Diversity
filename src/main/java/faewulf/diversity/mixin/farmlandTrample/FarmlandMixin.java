package faewulf.diversity.mixin.farmlandTrample;

import faewulf.diversity.util.ModConfigs;
import faewulf.diversity.util.converter;
import net.minecraft.block.BlockState;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FarmlandBlock.class)
public class FarmlandMixin {

    @Inject(method = "setToDirt", at = @At("HEAD"), cancellable = true)
    private static void setToDirtMixin(Entity entity, BlockState state, World world, BlockPos pos, CallbackInfo ci) {

        if (!ModConfigs.prevent_farmland_trampling)
            return;

        if (entity instanceof LivingEntity livingEntity) {
            if (EnchantmentHelper.getEquipmentLevel(converter.getEnchant(world, Enchantments.FEATHER_FALLING), livingEntity) > 0 || livingEntity.hasStatusEffect(StatusEffects.SLOW_FALLING)) {
                ci.cancel();
            }
        }
    }

}
