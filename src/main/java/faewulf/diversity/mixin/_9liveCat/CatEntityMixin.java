package faewulf.diversity.mixin._9liveCat;

import faewulf.diversity.inter.entity.ICustomCatEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.VariantHolder;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.CatVariant;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CatEntity.class)
public abstract class CatEntityMixin extends TameableEntity implements VariantHolder<RegistryEntry<CatVariant>>, ICustomCatEntity {
    protected CatEntityMixin(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
    }

    @Unique
    private int lives = 8;

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        nbt.putInt("diversity:lives", this.lives);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
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
