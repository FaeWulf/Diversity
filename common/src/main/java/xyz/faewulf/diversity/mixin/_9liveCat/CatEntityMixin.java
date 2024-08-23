package xyz.faewulf.diversity.mixin._9liveCat;

import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.VariantHolder;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.CatVariant;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.faewulf.diversity.inter.entity.ICustomCatEntity;

@Mixin(Cat.class)
public abstract class CatEntityMixin extends TamableAnimal implements VariantHolder<Holder<CatVariant>>, ICustomCatEntity {
    protected CatEntityMixin(EntityType<? extends TamableAnimal> entityType, Level world) {
        super(entityType, world);
    }

    @Unique
    private int lives = 8;

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void addAdditionalSaveData(CompoundTag nbt, CallbackInfo ci) {
        nbt.putInt("diversity:lives", this.lives);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readAdditionalSaveData(CompoundTag nbt, CallbackInfo ci) {
        if (nbt.contains("diversity:lives")) {
            this.lives = nbt.getInt("diversity:lives");
        } else
            this.lives = 9;
    }

    @Override
    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        if (lives < 0)
            lives = 0;

        this.lives = lives;
    }
}