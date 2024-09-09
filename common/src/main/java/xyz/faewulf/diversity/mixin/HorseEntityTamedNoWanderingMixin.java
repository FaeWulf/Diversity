package xyz.faewulf.diversity.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.animal.horse.Donkey;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.animal.horse.Mule;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import xyz.faewulf.diversity.util.config.ModConfigs;

@Mixin(LivingEntity.class)
public class HorseEntityTamedNoWanderingMixin {

    //easy, just modify value of the travel() method into ZERO
    @ModifyArg(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;travel(Lnet/minecraft/world/phys/Vec3;)V"))
    private Vec3 tickMovementModifyArgs(Vec3 movementInput) {

        if (!ModConfigs.prevent_tamed_horse_wandering)
            return movementInput;

        if ((Object) this instanceof Horse horseEntity) {
            if (horseEntity.isTamed() && horseEntity.isSaddled()) {
                return Vec3.ZERO;
            }
        }

        if ((Object) this instanceof Donkey donkeyEntity) {
            if (donkeyEntity.isTamed() && donkeyEntity.isSaddled()) {
                return Vec3.ZERO;
            }
        }

        if ((Object) this instanceof Mule muleEntity) {
            if (muleEntity.isTamed() && muleEntity.isSaddled()) {
                return Vec3.ZERO;
            }
        }

        if ((Object) this instanceof Camel camel) {
            if (camel.isTamed() && camel.isSaddled()) {
                return Vec3.ZERO;
            }
        }

        return movementInput;
    }
}
