package xyz.faewulf.diversity.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.faewulf.diversity.inter.IPlayerDataSaver;

@Mixin(Entity.class)
public class PlayerDataSaver implements IPlayerDataSaver {
    @Unique
    private CompoundTag persistentData;

    @Override
    public CompoundTag getPersistentData() {
        if (this.persistentData == null) {
            this.persistentData = new CompoundTag();
        }

        return this.persistentData;
    }

    @Inject(method = "saveWithoutId", at = @At("HEAD"))
    protected void writeNbt(CompoundTag nbt, CallbackInfoReturnable info) {
        if (persistentData != null) {
            nbt.put("faewulf.diversity", persistentData);
        }
    }

    @Inject(method = "load", at = @At("HEAD"))
    protected void readNbt(CompoundTag nbt, CallbackInfo info) {
        if (nbt.contains("faewulf.diversity", 10)) {
            persistentData = nbt.getCompound("faewulf.diversity");
        }
    }
}
