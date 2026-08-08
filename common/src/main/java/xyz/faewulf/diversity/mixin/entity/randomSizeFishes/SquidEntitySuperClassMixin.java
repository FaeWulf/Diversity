package xyz.faewulf.diversity.mixin.entity.randomSizeFishes;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.faewulf.diversity.inter.entity.ICustomSquidEntity;

@Mixin(Mob.class)
public class SquidEntitySuperClassMixin {

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void addAdditionalSaveDataInject(ValueOutput valueOutput, CallbackInfo ci) {
        if ((Object) this instanceof ICustomSquidEntity iCustomSquidEntity) {
            valueOutput.putFloat("diversity:Size", iCustomSquidEntity.diversity_Multiloader$getSize());
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readAdditionalSaveDataInject(ValueInput valueInput, CallbackInfo ci) {
        if ((Object) this instanceof ICustomSquidEntity iCustomSquidEntity) {
            iCustomSquidEntity.diversity_Multiloader$setSize(valueInput.getFloatOr("diversity:Size", 1f));
            iCustomSquidEntity.diversity_Multiloader$reCalculateSize();
        }
    }
}
