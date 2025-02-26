package xyz.faewulf.diversity.mixin.entity.preventSaddledMobMovement;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import xyz.faewulf.diversity.util.config.ModConfigs;

@Mixin(LivingEntity.class)
public abstract class ModdedEntityTamedNoWanderingMixin extends Entity implements Attackable {

    public ModdedEntityTamedNoWanderingMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    // Universal method for other mob (modded mob)
    // modify value of the travel() method into ZERO if it is saddleable mob, and currently saddled
    @WrapOperation(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;travel(Lnet/minecraft/world/phys/Vec3;)V"))
    private void tickMovementModifyArgs(LivingEntity instance, Vec3 vec3, Operation<Void> original) {

        if (!ModConfigs.prevent_tamed_horse_wandering) {
            original.call(instance, vec3);
            return;
        }

        if ((Object) this instanceof Mob mob) {


            if (mob instanceof Saddleable saddleable &&
                    !EntityType.getKey(mob.getType()).getNamespace().equals("minecraft")
            ) {
                if (saddleable.isSaddled()) {
                    original.call(instance, Vec3.ZERO);
                    return;
                }
            }
        }

        // call original if none
        original.call(instance, vec3);
    }
}
