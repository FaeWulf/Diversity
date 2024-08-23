package xyz.faewulf.diversity.mixin.invisibleItemFrame;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.faewulf.diversity.inter.ICustomItemFrame;

@Mixin(ItemFrame.class)
public class InvisibleItemFrame implements ICustomItemFrame {
    @Unique
    private boolean isInvisible;

    //add invisible if holding item
    @Inject(method = "setItem(Lnet/minecraft/world/item/ItemStack;Z)V", at = @At("TAIL"))
    private void setHeldItem(ItemStack value, boolean update, CallbackInfo ci) {
        if (this.isInvisible)
            ((ItemFrame) (Object) this).setInvisible(true);
    }

    //remove invisible if no item holding
    @Inject(method = "removeFramedMap", at = @At("TAIL"))
    private void removeFromFrameMixin(ItemStack stack, CallbackInfo ci) {
        if (this.isInvisible)
            ((ItemFrame) (Object) this).setInvisible(false);
    }

    //onBreak
    @Inject(at = @At("HEAD"), method = "dropItem(Lnet/minecraft/world/entity/Entity;)V")
    private void onBreak(Entity entity, CallbackInfo ci) {
        if (isInvisible) {
            ItemStack extraItem = new ItemStack(Items.GLASS_PANE);
            ((ItemFrame) (Object) this).spawnAtLocation(extraItem);
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void addAdditionalSaveDataInject(CompoundTag nbt, CallbackInfo ci) {
        nbt.putBoolean("isInvisible", this.isInvisible);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readAdditionalSaveDataInject(CompoundTag nbt, CallbackInfo ci) {
        if (nbt.contains("isInvisible")) {
            this.isInvisible = nbt.getBoolean("isInvisible");
        }
    }

    public boolean getIsInvisible() {
        return isInvisible;
    }

    public void setIsInvisible(boolean isInvisible) {
        this.isInvisible = isInvisible;
    }

}
