package xyz.faewulf.diversity.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
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
    protected void writeNbt(ValueOutput valueOutput, CallbackInfo ci) {
        if (diversity_Multiloader$persistentData != null) {
            valueOutput.storeNullable("faewulf.diversity", CompoundTag.CODEC, diversity_Multiloader$persistentData);
        }
    }

    @Inject(method = "load", at = @At("HEAD"))
    protected void readNbt(ValueInput valueInput, CallbackInfo ci) {
        diversity_Multiloader$persistentData = valueInput.read("faewulf.diversity", CompoundTag.CODEC).orElse(null);
    }
}
