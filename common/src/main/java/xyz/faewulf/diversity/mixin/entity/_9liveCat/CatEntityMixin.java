package xyz.faewulf.diversity.mixin.entity._9liveCat;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.feline.Cat;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.faewulf.diversity.inter.entity.ICustomCatEntity;

@Mixin(Cat.class)
public abstract class CatEntityMixin extends TamableAnimal implements ICustomCatEntity {
    @Unique
    private int diversity_Multiloader$lives = 8;

    protected CatEntityMixin(EntityType<? extends TamableAnimal> entityType, Level world) {
        super(entityType, world);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void addAdditionalSaveData(ValueOutput valueOutput, CallbackInfo ci) {
        valueOutput.putInt("diversity:lives", this.diversity_Multiloader$lives);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readAdditionalSaveData(ValueInput valueInput, CallbackInfo ci) {
        this.diversity_Multiloader$lives = valueInput.getInt("diversity:lives").orElse(9);
    }

    @Override
    public int diversity_Multiloader$getLives() {
        return diversity_Multiloader$lives;
    }

    public void diversity_Multiloader$setLives(int lives) {
        if (lives < 0)
            lives = 0;

        this.diversity_Multiloader$lives = lives;
    }
}
