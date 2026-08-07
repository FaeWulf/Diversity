package xyz.faewulf.diversity.mixin.entity.featherOnBrush;

import net.minecraft.world.entity.animal.chicken.Chicken;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.faewulf.diversity.inter.entity.ICustomChickenEntity;

@Mixin(Chicken.class)
public class ChickenEntityMixin implements ICustomChickenEntity {

    @Unique
    private int diversity_Multiloader$featherCoolDown = 0;

    @Inject(method = "aiStep", at = @At("HEAD"))
    private void tickMovementMixin(CallbackInfo ci) {
        if (diversity_Multiloader$featherCoolDown > 0) {
            diversity_Multiloader$featherCoolDown--;
        }
    }


    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void addAdditionalSaveData(ValueOutput valueOutput, CallbackInfo ci) {
        valueOutput.putInt("diversity:featherCoolDown", this.diversity_Multiloader$featherCoolDown);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readAdditionalSaveData(ValueInput valueInput, CallbackInfo ci) {
        this.diversity_Multiloader$featherCoolDown = valueInput.getInt("diversity:featherCoolDown").orElse(0);
    }

    @Override
    public void diversity_Multiloader$setFeatherCoolDown(int value) {
        this.diversity_Multiloader$featherCoolDown = value;
    }

    @Override
    public int diversity_Multiloader$getFeatherCoolDown() {
        return this.diversity_Multiloader$featherCoolDown;
    }
}
