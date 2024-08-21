package faewulf.diversity.mixin.randomSizeFishes;


//? if >=1.21 {

/*import faewulf.diversity.inter.entity.ICustomSquidEntity;
import faewulf.diversity.util.ModConfigs;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.passive.SquidEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SquidEntity.class)
public abstract class SquidEntityMixin extends WaterCreatureEntity implements ICustomSquidEntity {

    @Unique
    private float size = (float) (this.random.nextGaussian() * 0.2 + 1.2);

    protected SquidEntityMixin(EntityType<? extends WaterCreatureEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void initInject(EntityType<? extends SquidEntity> entityType, World world, CallbackInfo ci) {
        reCalculateSize();
    }

    @Unique
    @Override
    public void reCalculateSize() {

        if (size < 0.6f)
            size = 0.6f;

        if (!ModConfigs.random_size_fishes)
            size = 1.0f;

        EntityAttributeInstance entityAttributeInstance = this.getAttributes().getCustomInstance(EntityAttributes.GENERIC_SCALE);

        if (entityAttributeInstance != null) {
            entityAttributeInstance.setBaseValue(size);
        }
    }

    @Override
    public void setSize(float size) {
        this.size = size;
    }

    @Override
    public float getSize() {
        return size;
    }
}
 
*///?}