package faewulf.diversity.mixin;

import faewulf.diversity.util.ModConfigs;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.DonkeyEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.passive.MuleEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LivingEntity.class)
public class HorseEntityTamedNoWanderingMixin {

    //easy, just modify value of the travel() method into ZERO
    @ModifyArg(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;travel(Lnet/minecraft/util/math/Vec3d;)V"))
    private Vec3d tickMovementModifyArgs(Vec3d movementInput) {

        if (!ModConfigs.prevent_tamed_horse_wandering)
            return movementInput;

        if ((Object) this instanceof HorseEntity horseEntity) {
            if (horseEntity.isTame() && horseEntity.isSaddled()) {
                return Vec3d.ZERO;
            }
        }

        if ((Object) this instanceof DonkeyEntity donkeyEntity) {
            if (donkeyEntity.isTame() && donkeyEntity.isSaddled()) {
                return Vec3d.ZERO;
            }
        }

        if ((Object) this instanceof MuleEntity muleEntity) {
            if (muleEntity.isTame() && muleEntity.isSaddled()) {
                return Vec3d.ZERO;
            }
        }

        return movementInput;
    }
}
