package faewulf.diversity.mixin.clickThrough;

import faewulf.diversity.util.BlockEntityContainer;
import faewulf.diversity.util.ModConfigs;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemFrameEntity.class)
public class ItemFrame {

    @Inject(method = "interact", at = @At("HEAD"), cancellable = true)
    private void interact(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {

        if (!ModConfigs.click_through_itemframe)
            return;

        if (player.getWorld().isClient)
            return;

        //cast type
        ItemFrameEntity _this = (ItemFrameEntity) (Object) this;

        //if one of these below is true:
        // player is sneaking
        // no Item in ItemFrame
        // then don't trigger clickthrough function
        if (_this.getHeldItemStack().isEmpty() || player.isSneaking()) return;


        //this will get the position of the block that item attached to
        BlockPos posBehindItemFrame = BlockPos.ofFloored(_this.getPos().offset(_this.getFacing().getOpposite(), 1));

        boolean success = BlockEntityContainer.tryOpenContainer(posBehindItemFrame, player);

        if (success) {
            cir.setReturnValue(ActionResult.CONSUME);
            cir.cancel();
        }


    }
}
