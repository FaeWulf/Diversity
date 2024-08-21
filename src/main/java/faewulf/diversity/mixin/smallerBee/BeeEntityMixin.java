package faewulf.diversity.mixin.smallerBee;

//? if >=1.21 {

/*import faewulf.diversity.util.ModConfigs;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Flutterer;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BeeEntity.class)
public abstract class BeeEntityMixin extends AnimalEntity implements Angerable, Flutterer {

    protected BeeEntityMixin(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void initInject(EntityType<? extends BeeEntity> entityType, World world, CallbackInfo ci) {
        EntityAttributeInstance entityAttributeInstance = this.getAttributes().getCustomInstance(EntityAttributes.GENERIC_SCALE);

        if (entityAttributeInstance != null) {
            if (!ModConfigs.smaller_bee) {
                entityAttributeInstance.setBaseValue(1.0f);
                return;
            }
            entityAttributeInstance.setBaseValue(0.5f);
        }
    }

}

*///?}
