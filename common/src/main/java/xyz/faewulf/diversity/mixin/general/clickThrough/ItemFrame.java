package xyz.faewulf.diversity.mixin.general.clickThrough;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.faewulf.diversity.util.BlockEntityContainer;
import xyz.faewulf.diversity.util.config.ModConfigs;

@Mixin(net.minecraft.world.entity.decoration.ItemFrame.class)
public class ItemFrame {

    @Inject(method = "interact", at = @At("HEAD"), cancellable = true)
    private void interact(Player player, InteractionHand hand, Vec3 location, CallbackInfoReturnable<InteractionResult> cir) {

        if (!ModConfigs.click_through_itemframe)
            return;

        if (player.level().isClientSide())
            return;

        //cast type
        net.minecraft.world.entity.decoration.ItemFrame _this = (net.minecraft.world.entity.decoration.ItemFrame) (Object) this;

        //if one of these below is true:
        // player is sneaking
        // no Item in ItemFrame
        // then don't trigger clickthrough function
        if (_this.getItem().isEmpty() || player.isShiftKeyDown()) return;


        //this will get the position of the block that item attached to
        BlockPos posBehindItemFrame = BlockPos.containing(_this.position().relative(_this.getNearestViewDirection().getOpposite(), 1));

        boolean success = BlockEntityContainer.tryOpenContainer(posBehindItemFrame, player);

        if (success) {
            cir.setReturnValue(InteractionResult.CONSUME);
            cir.cancel();
        }


    }
}
