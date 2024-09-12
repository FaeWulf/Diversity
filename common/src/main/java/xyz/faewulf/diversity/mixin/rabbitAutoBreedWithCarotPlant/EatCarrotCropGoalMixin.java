package xyz.faewulf.diversity.mixin.rabbitAutoBreedWithCarotPlant;

import net.minecraft.world.entity.animal.Rabbit;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.faewulf.diversity.util.config.ModConfigs;

import static net.minecraft.world.entity.AgeableMob.getSpeedUpSecondsWhenFeeding;

@Mixin(targets = "net.minecraft.world.entity.animal.Rabbit.RaidGardenGoal")
public class EatCarrotCropGoalMixin {

    @Shadow
    @Final
    private Rabbit rabbit;

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getValue(Lnet/minecraft/world/level/block/state/properties/Property;)Ljava/lang/Comparable;", shift = At.Shift.AFTER))
    private void tickInject(CallbackInfo ci) {

        if (!ModConfigs.rabbit_eat_carrot_crops)
            return;

        if (!this.rabbit.isBaby()) {
            if (this.rabbit.getAge() <= 0)
                this.rabbit.setInLove(null);
        } else {
            this.rabbit.ageUp(getSpeedUpSecondsWhenFeeding(-this.rabbit.getAge()), true);
        }
    }
}
