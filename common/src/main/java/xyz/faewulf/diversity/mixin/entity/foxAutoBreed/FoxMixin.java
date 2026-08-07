package xyz.faewulf.diversity.mixin.entity.foxAutoBreed;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.faewulf.diversity.util.config.ModConfigs;

@Mixin(Fox.class)
public abstract class FoxMixin extends Animal {
    protected FoxMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;finishUsingItem(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;)Lnet/minecraft/world/item/ItemStack;"))
    private void aiStepInject(CallbackInfo ci) {

        if (!ModConfigs.fox_auto_breed)
            return;

        if (!this.isBaby()) {
            if (this.getAge() <= 0)
                this.setInLove(null);
        } else {
            this.ageUp(getSpeedUpSecondsWhenFeeding(-this.getAge()), true);
        }
    }
}
