package xyz.faewulf.diversity.mixin.randomSizeFishes;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.faewulf.diversity.inter.entity.ICustomSquidEntity;

@Mixin(Mob.class)
public class SquidEntitySuperClassMixin {

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void addAdditionalSaveDataInject(CompoundTag nbt, CallbackInfo ci) {
        if ((Object) this instanceof ICustomSquidEntity iCustomSquidEntity) {
            nbt.putFloat("diversity:Size", iCustomSquidEntity.getSize());
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readAdditionalSaveDataInject(CompoundTag nbt, CallbackInfo ci) {
        if ((Object) this instanceof ICustomSquidEntity iCustomSquidEntity) {
            if (nbt.contains("diversity:Size")) {
                iCustomSquidEntity.setSize(nbt.getFloat("diversity:Size"));
            }
            iCustomSquidEntity.reCalculateSize();
        }
    }


}
