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
    private CompoundTag diversity_Multiloader$persistentData;

    @Override
    public CompoundTag diversity_Multiloader$getPersistentData() {
        if (this.diversity_Multiloader$persistentData == null) {
            this.diversity_Multiloader$persistentData = new CompoundTag();
        }

        return this.diversity_Multiloader$persistentData;
    }

    @Inject(method = "saveWithoutId", at = @At("HEAD"))
    protected void writeNbt(CompoundTag nbt, CallbackInfoReturnable<CompoundTag> cir) {
        if (diversity_Multiloader$persistentData != null) {
            nbt.put("faewulf.diversity", diversity_Multiloader$persistentData);
        }
    }

    @Inject(method = "load", at = @At("HEAD"))
    protected void readNbt(CompoundTag nbt, CallbackInfo info) {
        if (nbt.contains("faewulf.diversity", 10)) {
            diversity_Multiloader$persistentData = nbt.getCompound("faewulf.diversity");
        }
    }
}
