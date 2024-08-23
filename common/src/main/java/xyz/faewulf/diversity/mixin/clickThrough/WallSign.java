package xyz.faewulf.diversity.mixin.clickThrough;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.faewulf.diversity.util.BlockEntityContainer;
import xyz.faewulf.diversity.util.ModConfigs;

import static net.minecraft.world.level.block.WallSignBlock.FACING;

@Mixin(ServerPlayer.class)
public abstract class WallSign extends Player {
    public WallSign(Level world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Inject(method = "openTextEdit", at = @At("HEAD"), cancellable = true)
    private void openEditSignScreenMixin(SignBlockEntity sign, boolean front, CallbackInfo ci) {

        if (!ModConfigs.click_through_itemframe)
            return;

        //if player is sneaking then return
        if (this.isShiftKeyDown())
            return;

        //if it is a wall sign
        if (sign.getBlockState().getBlock() instanceof WallSignBlock wallSignBlock) {
            BlockPos posBehindItemFrame = sign.getBlockPos().relative(sign.getBlockState().getValue(FACING).getOpposite(), 1);

            boolean success = BlockEntityContainer.tryOpenContainer(posBehindItemFrame, this);

            if (success) {
                ci.cancel();
            }
        }
    }
}
