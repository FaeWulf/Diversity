package xyz.faewulf.diversity.mixin.entity.preventSaddledMobMovement;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Saddleable;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.faewulf.diversity.util.config.ModConfigs;

@Mixin(RandomStrollGoal.class)
public abstract class RandomStrollGoalMixin extends Goal {

    @Shadow
    @Final
    protected PathfinderMob mob;

    @Inject(method = "getPosition", at = @At("HEAD"), cancellable = true)
    private void getPositionInject(CallbackInfoReturnable<Vec3> cir) {
        if (this.mob instanceof Saddleable saddleable && ModConfigs.prevent_tamed_horse_wandering) {
            if (saddleable.isSaddled()) {
                cir.setReturnValue(null);
                cir.cancel();
            }
        }
    }
}
