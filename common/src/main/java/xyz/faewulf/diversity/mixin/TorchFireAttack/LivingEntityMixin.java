package xyz.faewulf.diversity.mixin.TorchFireAttack;

import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Attackable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.faewulf.diversity.util.ModConfigs;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Attackable {

    @Shadow
    public abstract void igniteForTicks(int ticks);

    @Shadow
    protected abstract void checkFallDamage(double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition);

    public LivingEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Inject(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;actuallyHurt(Lnet/minecraft/world/damagesource/DamageSource;F)V"))
    private void damageInject(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {

        if (!ModConfigs.torch_burn_target)
            return;

        Entity entityAttacker = source.getEntity();

        if (entityAttacker != null) {
            if (entityAttacker instanceof LivingEntity livingEntity) {
                livingEntity.getHandSlots().forEach(itemStack -> {
                    if (itemStack.getItem() == Items.TORCH || itemStack.getItem() == Items.SOUL_TORCH) {
                        this.igniteForTicks(100);
                        return;
                    }
                });
            }
        }
    }
}