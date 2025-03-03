package xyz.faewulf.diversity.mixin.general.torchFireAttack;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Attackable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.faewulf.lib.util.Compare;
import xyz.faewulf.diversity.util.config.ModConfigs;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Attackable {

    public LivingEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Shadow
    public abstract void igniteForTicks(int ticks);

    @Inject(method = "hurtServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;actuallyHurt(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)V"))
    private void damageInject(ServerLevel level, DamageSource source, float p_376610_, CallbackInfoReturnable<Boolean> cir) {

        if (!ModConfigs.torch_burn_target)
            return;

        Entity entityAttacker = source.getEntity();

        if (entityAttacker != null) {
            if (entityAttacker instanceof LivingEntity livingEntity) {
                livingEntity.getHandSlots().forEach(itemStack -> {
                    if (Compare.isHasTag(itemStack.getItem(), "diversity:flame_weapon")) {
                        this.igniteForTicks(100);
                    }
                });
            }
        }
    }
}
