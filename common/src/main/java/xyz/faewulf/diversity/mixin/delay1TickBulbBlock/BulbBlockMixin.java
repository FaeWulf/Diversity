package xyz.faewulf.diversity.mixin.delay1TickBulbBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CopperBulbBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.Orientation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.faewulf.diversity.util.config.ModConfigs;

@Mixin(CopperBulbBlock.class)
public abstract class BulbBlockMixin extends Block {
    @Shadow
    public abstract void checkAndFlip(BlockState state, ServerLevel world, BlockPos pos);

    public BulbBlockMixin(Properties settings) {
        super(settings);
    }

    @Inject(method = "neighborChanged", at = @At("HEAD"), cancellable = true)
    private void updateInject(BlockState blockState, Level level, BlockPos blockPos, Block block, Orientation orientation, boolean p_309085_, CallbackInfo ci) {
        if (!ModConfigs.copper_bulb_tick_delay)
            return;

        level.scheduleTick(blockPos, level.getBlockState(blockPos).getBlock(), 1);
        ci.cancel();
    }

    protected void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        this.checkAndFlip(state, world, pos);
    }
}
