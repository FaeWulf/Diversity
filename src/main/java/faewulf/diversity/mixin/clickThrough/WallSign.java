package faewulf.diversity.mixin.clickThrough;

import com.mojang.authlib.GameProfile;
import faewulf.diversity.util.BlockEntityContainer;
import faewulf.diversity.util.ModConfigs;
import net.minecraft.block.WallSignBlock;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.block.WallSignBlock.FACING;

@Mixin(ServerPlayerEntity.class)
public abstract class WallSign extends PlayerEntity {
    public WallSign(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Inject(method = "openEditSignScreen", at = @At("HEAD"), cancellable = true)
    private void openEditSignScreenMixin(SignBlockEntity sign, boolean front, CallbackInfo ci) {

        if (!ModConfigs.click_through_itemframe)
            return;

        //if player is sneaking then return
        if (this.isSneaking())
            return;

        //if it is a wall sign
        if (sign.getCachedState().getBlock() instanceof WallSignBlock wallSignBlock) {
            BlockPos posBehindItemFrame = sign.getPos().offset(sign.getCachedState().get(FACING).getOpposite(), 1);

            boolean success = BlockEntityContainer.tryOpenContainer(posBehindItemFrame, this);

            if (success) {
                ci.cancel();
            }
        }
    }
}
