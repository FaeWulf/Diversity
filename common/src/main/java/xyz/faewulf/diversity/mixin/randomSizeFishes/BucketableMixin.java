package xyz.faewulf.diversity.mixin.randomSizeFishes;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.faewulf.diversity.inter.entity.ICustomAbstractFish;

@Mixin(Bucketable.class)
public interface BucketableMixin {

    @Inject(method = "saveDefaultDataToBucketTag", at = @At("HEAD"))
    private static void saveDefaultDataToBucketTagInject(Mob mob, ItemStack stack, CallbackInfo ci) {
        if (mob instanceof ICustomAbstractFish iCustomAbstractFish) {
            CustomData.update(DataComponents.BUCKET_ENTITY_DATA, stack, nbtCompound -> nbtCompound.putFloat("diversity:Size", iCustomAbstractFish.diversity_Multiloader$getSize()));
        }
    }

    @Inject(method = "loadDefaultDataFromBucketTag", at = @At("HEAD"))
    private static void loadDefaultDataFromBucketTagInject(Mob mob, CompoundTag nbt, CallbackInfo ci) {

        if (mob instanceof ICustomAbstractFish iCustomAbstractFish) {
            if (nbt.contains("diversity:Size")) {
                iCustomAbstractFish.diversity_Multiloader$setSize(nbt.getFloat("diversity:Size"));
            }
            iCustomAbstractFish.diversity_Multiloader$reCalculateSize();
        }
    }
}
