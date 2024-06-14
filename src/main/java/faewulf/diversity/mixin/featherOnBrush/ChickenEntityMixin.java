package faewulf.diversity.mixin.featherOnBrush;

import faewulf.diversity.inter.entity.ICustomChickenEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChickenEntity.class)
public class ChickenEntityMixin implements ICustomChickenEntity {

    @Unique
    private int featherCoolDown = 0;

    @Inject(method = "tickMovement", at = @At("HEAD"))
    private void tickMovementMixin(CallbackInfo ci) {
        if (featherCoolDown > 0) {
            featherCoolDown--;
        }
    }


    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        nbt.putInt("diversity:featherCoolDown", this.featherCoolDown);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("diversity:featherCoolDown", NbtElement.NUMBER_TYPE)) {
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
