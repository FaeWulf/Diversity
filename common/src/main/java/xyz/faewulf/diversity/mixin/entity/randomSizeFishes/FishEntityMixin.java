package xyz.faewulf.diversity.mixin.entity.randomSizeFishes;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Bucketable;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.fish.AbstractFish;
import net.minecraft.world.entity.animal.fish.Salmon;
import net.minecraft.world.entity.animal.fish.WaterAnimal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.faewulf.diversity.inter.entity.ICustomAbstractFish;
import xyz.faewulf.diversity.util.config.ModConfigs;

@Mixin(AbstractFish.class)
public abstract class FishEntityMixin extends WaterAnimal implements Bucketable, ICustomAbstractFish {
    @Unique
    private float diversity_Multiloader$size = (float) (this.random.nextGaussian() * 0.2 + 1);

    protected FishEntityMixin(EntityType<? extends WaterAnimal> entityType, Level world) {
        super(entityType, world);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void initInject(EntityType<? extends Salmon> entityType, Level world, CallbackInfo ci) {
        diversity_Multiloader$reCalculateSize();
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void addAdditionalSaveDataInject(ValueOutput valueOutput, CallbackInfo ci) {
        valueOutput.putFloat("diversity:Size", diversity_Multiloader$size);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readAdditionalSaveDataInject(ValueInput valueInput, CallbackInfo ci) {
        this.diversity_Multiloader$size = valueInput.getFloatOr("diversity:Size", 1f);
        diversity_Multiloader$reCalculateSize();
    }

    @Inject(method = "saveToBucketTag", at = @At("TAIL"))
    private void saveToBucketTagInject(ItemStack stack, CallbackInfo ci) {
        CustomData.update(DataComponents.BUCKET_ENTITY_DATA, stack, nbtCompound -> nbtCompound.putFloat("diversity:Size", diversity_Multiloader$getSize()));
    }

    @Inject(method = "loadFromBucketTag", at = @At("TAIL"))
    private void loadFromBucketTagInject(CompoundTag tag, CallbackInfo ci) {
        if (tag.contains("diversity:Size")) {
            diversity_Multiloader$setSize(tag.getFloat("diversity:Size").orElse(1f));
        }
        diversity_Multiloader$reCalculateSize();
    }

    @Override
    public float diversity_Multiloader$getSize() {
        return this.diversity_Multiloader$size;
    }

    @Override
    public void diversity_Multiloader$setSize(float value) {
        this.diversity_Multiloader$size = value;
    }

    @Override
    public void diversity_Multiloader$reCalculateSize() {
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
