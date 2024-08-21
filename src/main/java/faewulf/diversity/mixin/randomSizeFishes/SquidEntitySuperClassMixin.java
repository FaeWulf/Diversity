package faewulf.diversity.mixin.randomSizeFishes;

//? if >=1.21 {

/*import faewulf.diversity.inter.entity.ICustomSquidEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobEntity.class)
public class SquidEntitySuperClassMixin {

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void writeCustomDataToNbtInject(NbtCompound nbt, CallbackInfo ci) {
        if ((Object) this instanceof ICustomSquidEntity iCustomSquidEntity) {
            nbt.putFloat("diversity:Size", iCustomSquidEntity.getSize());
        }
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void readCustomDataFromNbtInject(NbtCompound nbt, CallbackInfo ci) {
        if ((Object) this instanceof ICustomSquidEntity iCustomSquidEntity) {
            if (nbt.contains("diversity:Size")) {
                iCustomSquidEntity.setSize(nbt.getFloat("diversity:Size"));
            }
            iCustomSquidEntity.reCalculateSize();
        }
    }


}
 
*///?}
