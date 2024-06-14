package faewulf.diversity.mixin.invisibleItemFrame;

import faewulf.diversity.inter.ICustomItemFrame;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemFrameEntity.class)
public class InvisibleItemFrame implements ICustomItemFrame {
    @Unique
    private boolean isInvisible;

    //add invisible if holding item
    @Inject(method = "setHeldItemStack(Lnet/minecraft/item/ItemStack;Z)V", at = @At("TAIL"))
    private void setHeldItem(ItemStack value, boolean update, CallbackInfo ci) {
        if (this.isInvisible)
            ((ItemFrameEntity) (Object) this).setInvisible(true);
    }

    //remove invisible if no item holding
    @Inject(method = "removeFromFrame", at = @At("TAIL"))
    private void removeFromFrameMixin(ItemStack stack, CallbackInfo ci) {
        if (this.isInvisible)
            ((ItemFrameEntity) (Object) this).setInvisible(false);
    }

    //onBreak
    @Inject(at = @At("HEAD"), method = "onBreak(Lnet/minecraft/entity/Entity;)V")
    private void onBreak(Entity entity, CallbackInfo ci) {
        if (isInvisible) {
            ItemStack extraItem = new ItemStack(Items.GLASS_PANE);
            ((ItemFrameEntity) (Object) this).dropStack(extraItem);
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void writeCustomDataToNbtInject(NbtCompound nbt, CallbackInfo ci) {
        nbt.putBoolean("isInvisible", this.isInvisible);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void readCustomDataFromNbtInject(NbtCompound nbt, CallbackInfo ci) {
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
