package xyz.faewulf.diversity.mixin.randomSizeFishes;

import xyz.faewulf.diversity.inter.entity.ICustomSquidEntity;
import xyz.faewulf.diversity.util.ModConfigs;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Squid.class)
public abstract class SquidEntityMixin extends WaterAnimal implements ICustomSquidEntity {

    @Unique
    private float size = (float) (this.random.nextGaussian() * 0.2 + 1.2);

    protected SquidEntityMixin(EntityType<? extends WaterAnimal> entityType, Level world) {
        super(entityType, world);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void initInject(EntityType<? extends Squid> entityType, Level world, CallbackInfo ci) {
        reCalculateSize();
    }

    @Unique
    @Override
    public void reCalculateSize() {

        if (size < 0.6f)
            size = 0.6f;

        if (!ModConfigs.random_size_fishes)
            size = 1.0f;

        AttributeInstance entityAttributeInstance = this.getAttributes().getInstance(Attributes.SCALE);

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
