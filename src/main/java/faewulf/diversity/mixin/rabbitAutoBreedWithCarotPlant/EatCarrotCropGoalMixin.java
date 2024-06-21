package faewulf.diversity.mixin.rabbitAutoBreedWithCarotPlant;

import net.minecraft.entity.passive.RabbitEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.entity.passive.PassiveEntity.toGrowUpAge;

@Mixin(targets = "net.minecraft.entity.passive.RabbitEntity$EatCarrotCropGoal")
public class EatCarrotCropGoalMixin {

    @Shadow
    @Final
    private RabbitEntity rabbit;

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;get(Lnet/minecraft/state/property/Property;)Ljava/lang/Comparable;", shift = At.Shift.AFTER))
    private void tickInject(CallbackInfo ci) {
        if (!this.rabbit.isBaby()) {
            if (this.rabbit.getBreedingAge() <= 0)
                this.rabbit.lovePlayer(null);
        } else {
            this.rabbit.growUp(toGrowUpAge(-this.rabbit.getBreedingAge()), true);
        }
    }
}
