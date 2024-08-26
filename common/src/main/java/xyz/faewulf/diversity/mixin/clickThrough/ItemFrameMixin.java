package xyz.faewulf.diversity.mixin.clickThrough;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.faewulf.diversity.util.BlockEntityContainer;
import xyz.faewulf.diversity.util.MissingMethod.EntityMethod;
import xyz.faewulf.diversity.util.ModConfigs;

@Mixin(ItemFrame.class)
public class ItemFrameMixin {

    @Inject(method = "interact", at = @At("HEAD"), cancellable = true)
    private void interact(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {

        if (!ModConfigs.click_through_itemframe)
            return;

        if (player.level().isClientSide)
            return;

        //cast type
        ItemFrame _this = (ItemFrame) (Object) this;

        //if one of these below is true:
        // player is sneaking
        // no Item in ItemFrameMixin
        // then don't trigger clickthrough function
        if (_this.getItem().isEmpty() || player.isShiftKeyDown()) return;

        //this will get the position of the block that item attached to

        //Equivalent for
        //BlockPos posBehindItemFrame = BlockPos.containing(_this.position().relative(_this.getNearestViewDirection().getOpposite(), 1));
        //:
        BlockPos posBehindItemFrame = BlockPos.containing(_this.position().relative(EntityMethod.getNearestViewDirection(_this).getOpposite(), 1));

        boolean success = BlockEntityContainer.tryOpenContainer(posBehindItemFrame, player);

        if (success) {
            cir.setReturnValue(InteractionResult.CONSUME);
            cir.cancel();
        }
    }
}
