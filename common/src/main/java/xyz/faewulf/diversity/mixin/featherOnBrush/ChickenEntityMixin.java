package xyz.faewulf.diversity.mixin.featherOnBrush;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.animal.Chicken;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.faewulf.diversity.inter.entity.ICustomChickenEntity;

@Mixin(Chicken.class)
public class ChickenEntityMixin implements ICustomChickenEntity {

    @Unique
    private int featherCoolDown = 0;

    @Inject(method = "aiStep", at = @At("HEAD"))
    private void tickMovementMixin(CallbackInfo ci) {
        if (featherCoolDown > 0) {
            featherCoolDown--;
        }
    }


    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void addAdditionalSaveData(CompoundTag nbt, CallbackInfo ci) {
        nbt.putInt("diversity:featherCoolDown", this.featherCoolDown);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readAdditionalSaveData(CompoundTag nbt, CallbackInfo ci) {
        if (nbt.contains("diversity:featherCoolDown", Tag.TAG_ANY_NUMERIC)) {
            this.featherCoolDown = nbt.getInt("diversity:featherCoolDown");
        }
    }

    @Override
    public void setFeatherCoolDown(int value) {
        this.featherCoolDown = value;
    }

    @Override
    public int getFeatherCoolDown() {
        return this.featherCoolDown;
    }
}
