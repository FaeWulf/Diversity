package xyz.faewulf.diversity.mixin.smallerBee;

import xyz.faewulf.diversity.util.config.ModConfigs;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Bee.class)
public abstract class BeeEntityMixin extends Animal implements NeutralMob, FlyingAnimal {

    protected BeeEntityMixin(EntityType<? extends Animal> entityType, Level world) {
        super(entityType, world);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void initInject(EntityType<? extends Bee> entityType, Level world, CallbackInfo ci) {
        AttributeInstance entityAttributeInstance = this.getAttributes().getInstance(Attributes.SCALE);

        if (entityAttributeInstance != null) {
            if (!ModConfigs.smaller_bee) {
                entityAttributeInstance.setBaseValue(1.0f);
                return;
            }
            entityAttributeInstance.setBaseValue(0.5f);
        }
    }

}
