package faewulf.diversity.mixin.delay1TickBulbBlock;

import faewulf.diversity.util.ModConfigs;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BulbBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BulbBlock.class)
public abstract class BulbBlobkMixin extends Block {
    @Shadow
    public abstract void update(BlockState state, ServerWorld world, BlockPos pos);

    public BulbBlobkMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "neighborUpdate", at = @At("HEAD"), cancellable = true)
    private void updatteInject(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify, CallbackInfo ci) {
        if (!ModConfigs.copper_bulb_tick_delay)
            return;

        world.scheduleBlockTick(pos, world.getBlockState(pos).getBlock(), 1);
        ci.cancel();
    }

    protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        this.update(state, world, pos);
    }
}
