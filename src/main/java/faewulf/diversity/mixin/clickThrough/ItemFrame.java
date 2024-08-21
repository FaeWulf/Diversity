package faewulf.diversity.mixin.clickThrough;

import faewulf.diversity.util.BlockEntityContainer;
import faewulf.diversity.util.ModConfigs;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemFrameEntity.class)
public abstract class ItemFrame extends AbstractDecorationEntity {

    @Shadow
    public abstract ItemStack getHeldItemStack();

    @Shadow
    public abstract int getRotation();

    protected ItemFrame(EntityType<? extends AbstractDecorationEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "interact", at = @At("HEAD"), cancellable = true)
    private void interact(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {

        if (!ModConfigs.click_through_itemframe)
            return;

        if (player.getWorld().isClient)
            return;

        //if one of these below is true:
        // player is sneaking
        // no Item in ItemFrame
        // then don't trigger clickthrough function
        if (this.getHeldItemStack().isEmpty() || player.isSneaking()) return;

        //this will get the position of the block that item attached to
        //? if >=1.21 {
        /*BlockPos posBehindItemFrame = BlockPos.ofFloored(this.getPos().offset(this.getFacing().getOpposite(), 1));
         *///?}

        //? if =1.20.1 {
        BlockPos posBehindItemFrame = BlockPos.ofFloored(this.getPos().offset(this.facing.getOpposite(), 1));
        //?}

        boolean success = BlockEntityContainer.tryOpenContainer(posBehindItemFrame, player);

        if (success) {
            cir.setReturnValue(ActionResult.CONSUME);
            cir.cancel();
        }


    }
}
