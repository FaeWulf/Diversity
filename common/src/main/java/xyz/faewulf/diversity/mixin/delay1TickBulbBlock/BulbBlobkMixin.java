package xyz.faewulf.diversity.mixin.delay1TickBulbBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CopperBulbBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.faewulf.diversity.util.config.ModConfigs;

@Mixin(CopperBulbBlock.class)
public abstract class BulbBlobkMixin extends Block {
    @Shadow
    public abstract void checkAndFlip(BlockState state, ServerLevel world, BlockPos pos);

    public BulbBlobkMixin(Properties settings) {
        super(settings);
    }

    @Inject(method = "neighborChanged", at = @At("HEAD"), cancellable = true)
    private void updatteInject(BlockState state, Level world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify, CallbackInfo ci) {
        if (!ModConfigs.copper_bulb_tick_delay)
            return;

        world.scheduleTick(pos, world.getBlockState(pos).getBlock(), 1);
        ci.cancel();
    }

    protected void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        this.checkAndFlip(state, world, pos);
    }
}
