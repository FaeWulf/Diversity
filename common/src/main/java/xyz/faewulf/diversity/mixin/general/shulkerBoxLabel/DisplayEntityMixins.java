package xyz.faewulf.diversity.mixin.general.shulkerBoxLabel;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Display;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.faewulf.diversity.inter.ICustomDisplayEntity;

import java.util.Objects;

@Mixin(Display.class)
public class DisplayEntityMixins implements ICustomDisplayEntity {

    @Unique
    int diversity_Multiloader$type;

    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo ci) {
        if (diversity_Multiloader$type == 1) {
            if ((Object) this instanceof Display.TextDisplay textDisplayEntity) {
                BlockPos blockPos = textDisplayEntity.blockPosition();
                Level world = textDisplayEntity.level();

                if (world.isClientSide())
                    return;

                BlockEntity blockEntity = world.getBlockEntity(blockPos);

                if (blockEntity instanceof ShulkerBoxBlockEntity shulkerBoxBlockEntity) {

                    //if somehow below block is replace by another shulker, then check the name
                    if (!Objects.equals(shulkerBoxBlockEntity.getName().getString(), textDisplayEntity.getCustomName().getString())) {
                        textDisplayEntity.discard();
                    }
                } else {
                    textDisplayEntity.discard();
                }
            }
        }
    }


    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void addAdditionalSaveData(ValueOutput valueOutput, CallbackInfo ci) {
        valueOutput.putInt("diversity:type", this.diversity_Multiloader$type);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readAdditionalSaveData(ValueInput valueInput, CallbackInfo ci) {
        this.diversity_Multiloader$type = valueInput.getInt("diversity:type").orElse(0);
    }

    @Override
    public int diversity_Multiloader$getType() {
        return this.diversity_Multiloader$type;
    }

    @Override
    public void diversity_Multiloader$setType(int type) {
        this.diversity_Multiloader$type = type;
    }

}
