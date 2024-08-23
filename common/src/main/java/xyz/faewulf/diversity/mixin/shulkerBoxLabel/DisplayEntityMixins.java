package xyz.faewulf.diversity.mixin.shulkerBoxLabel;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Display;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
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
    int type;

    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo ci) {
        if (type == 1) {
            if ((Object) this instanceof Display.TextDisplay textDisplayEntity) {
                BlockPos blockPos = textDisplayEntity.blockPosition();
                Level world = textDisplayEntity.level();

                if (world.isClientSide)
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
    private void addAdditionalSaveData(CompoundTag nbt, CallbackInfo ci) {
        nbt.putInt("diversity:type", this.type);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readAdditionalSaveData(CompoundTag nbt, CallbackInfo ci) {
        if (nbt.contains("diversity:type")) {
            this.type = nbt.getInt("diversity:type");
        }
    }

    public int getType() {
        return this.type;
    }

    @Override
    public void setType(int type) {
        this.type = type;
    }

}
