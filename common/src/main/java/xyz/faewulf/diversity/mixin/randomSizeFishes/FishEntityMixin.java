package xyz.faewulf.diversity.mixin.randomSizeFishes;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.animal.Salmon;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.faewulf.diversity.util.config.ModConfigs;

@Mixin(AbstractFish.class)
public abstract class FishEntityMixin extends WaterAnimal implements Bucketable {
    @Unique
    private float diversity_Multiloader$size = (float) (this.random.nextGaussian() * 0.2 + 1);

    protected FishEntityMixin(EntityType<? extends WaterAnimal> entityType, Level world) {
        super(entityType, world);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void initInject(EntityType<? extends Salmon> entityType, Level world, CallbackInfo ci) {
        diversity_Multiloader$reCalculateSize();
    }

    @Inject(method = "saveToBucketTag", at = @At("TAIL"))
    private void copyDataToStackInject(ItemStack stack, CallbackInfo ci) {
        CustomData.update(DataComponents.BUCKET_ENTITY_DATA, stack, nbtCompound -> nbtCompound.putFloat("diversity:Size", this.diversity_Multiloader$size));
    }

    @Inject(method = "loadFromBucketTag", at = @At("TAIL"))
    private void copyDataFromNbt(CompoundTag nbt, CallbackInfo ci) {
        if (nbt.contains("diversity:Size")) {
            this.diversity_Multiloader$size = nbt.getFloat("diversity:Size");
        }

        diversity_Multiloader$reCalculateSize();
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void addAdditionalSaveDataInject(CompoundTag nbt, CallbackInfo ci) {
        nbt.putFloat("diversity:Size", diversity_Multiloader$size);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readAdditionalSaveDataInject(CompoundTag nbt, CallbackInfo ci) {
        if (nbt.contains("diversity:Size")) {
            this.diversity_Multiloader$size = nbt.getFloat("diversity:Size");
        }
        diversity_Multiloader$reCalculateSize();
    }

    @Unique
    private void diversity_Multiloader$reCalculateSize() {
        if (diversity_Multiloader$size < 0.6f)
            diversity_Multiloader$size = 0.6f;

        if (diversity_Multiloader$size > 1.7f)
            diversity_Multiloader$size = 1.7f;

        if (!ModConfigs.random_size_fishes)
            diversity_Multiloader$size = 1.0f;

        AttributeInstance entityAttributeInstance = this.getAttributes().getInstance(Attributes.SCALE);

        if (entityAttributeInstance != null) {
            entityAttributeInstance.setBaseValue(diversity_Multiloader$size);
        }
    }
}
