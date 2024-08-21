package faewulf.diversity.mixin.randomSizeFishes;


//? if >=1.21 {

/*import faewulf.diversity.util.ModConfigs;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.Bucketable;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.passive.FishEntity;
import net.minecraft.entity.passive.SalmonEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FishEntity.class)
public abstract class FishEntityMixin extends WaterCreatureEntity implements Bucketable {
    protected FishEntityMixin(EntityType<? extends WaterCreatureEntity> entityType, World world) {
        super(entityType, world);
    }

    @Unique
    private float size = (float) (this.random.nextGaussian() * 0.2 + 1);

    @Inject(method = "<init>", at = @At("TAIL"))
    private void initInject(EntityType<? extends SalmonEntity> entityType, World world, CallbackInfo ci) {
        reCalculateSize();
    }

    @Inject(method = "copyDataToStack", at = @At("TAIL"))
    private void copyDataToStackInject(ItemStack stack, CallbackInfo ci) {
        NbtComponent.set(DataComponentTypes.BUCKET_ENTITY_DATA, stack, nbtCompound -> nbtCompound.putFloat("diversity:Size", this.size));
    }

    @Inject(method = "copyDataFromNbt", at = @At("TAIL"))
    private void copyDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("diversity:Size")) {
            this.size = nbt.getFloat("diversity:Size");
        }

        reCalculateSize();
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void writeCustomDataToNbtInject(NbtCompound nbt, CallbackInfo ci) {
        nbt.putFloat("diversity:Size", size);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void readCustomDataFromNbtInject(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("diversity:Size")) {
            this.size = nbt.getFloat("diversity:Size");
        }
        reCalculateSize();
    }

    @Unique
    private void reCalculateSize() {
        if (size < 0.6f)
            size = 0.6f;

        if (size > 1.7f)
            size = 1.7f;

        if (!ModConfigs.random_size_fishes)
            size = 1.0f;

        EntityAttributeInstance entityAttributeInstance = this.getAttributes().getCustomInstance(EntityAttributes.GENERIC_SCALE);

        if (entityAttributeInstance != null) {
            entityAttributeInstance.setBaseValue(size);
        }
    }
}
 
*///?}
