package faewulf.diversity.mixin.shulkerBoxLabel;

import faewulf.diversity.inter.ICustomDisplayEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(DisplayEntity.class)
public class DisplayEntityMixins implements ICustomDisplayEntity {

    @Unique
    int type;

    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo ci) {
        if (type == 1) {
            if ((Object) this instanceof DisplayEntity.TextDisplayEntity textDisplayEntity) {
                BlockPos blockPos = textDisplayEntity.getBlockPos();
                World world = textDisplayEntity.getWorld();

                if (world.isClient)
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


    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        nbt.putInt("diversity:type", this.type);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
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
