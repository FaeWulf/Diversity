package xyz.faewulf.diversity.mixin.torchFireAttack;

import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Attackable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.faewulf.diversity.util.compare;
import xyz.faewulf.diversity.util.config.ModConfigs;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Attackable {

    public LivingEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Shadow
    public abstract void igniteForTicks(int ticks);

    @Shadow
    protected abstract void checkFallDamage(double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition);

    @Inject(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;actuallyHurt(Lnet/minecraft/world/damagesource/DamageSource;F)V"))
    private void damageInject(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {

        if (!ModConfigs.torch_burn_target)
            return;

        Entity entityAttacker = source.getEntity();

        if (entityAttacker != null) {
            if (entityAttacker instanceof LivingEntity livingEntity) {
                livingEntity.getHandSlots().forEach(itemStack -> {
                    if (compare.isHasTag(itemStack.getItem(), "diversity:flame_weapon")) {
                        this.igniteForTicks(100);
                        return;
                    }
                });
            }
        }
    }
}
