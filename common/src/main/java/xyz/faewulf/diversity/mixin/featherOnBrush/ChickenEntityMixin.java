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
    private int multiLoader_1_20_1$featherCoolDown = 0;

    @Inject(method = "aiStep", at = @At("HEAD"))
    private void tickMovementMixin(CallbackInfo ci) {
        if (multiLoader_1_20_1$featherCoolDown > 0) {
            multiLoader_1_20_1$featherCoolDown--;
        }
    }


    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void addAdditionalSaveData(CompoundTag nbt, CallbackInfo ci) {
        nbt.putInt("diversity:featherCoolDown", this.multiLoader_1_20_1$featherCoolDown);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readAdditionalSaveData(CompoundTag nbt, CallbackInfo ci) {
        if (nbt.contains("diversity:featherCoolDown", Tag.TAG_ANY_NUMERIC)) {
            this.multiLoader_1_20_1$featherCoolDown = nbt.getInt("diversity:featherCoolDown");
        }
    }

    @Override
    public void multiLoader_1_20_1$setFeatherCoolDown(int value) {
        this.multiLoader_1_20_1$featherCoolDown = value;
    }

    @Override
    public int multiLoader_1_20_1$getFeatherCoolDown() {
        return this.multiLoader_1_20_1$featherCoolDown;
    }
}
