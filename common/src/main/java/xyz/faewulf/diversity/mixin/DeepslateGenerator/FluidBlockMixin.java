package xyz.faewulf.diversity.mixin.DeepslateGenerator;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import xyz.faewulf.diversity.util.config.ModConfigs;

@Mixin(LiquidBlock.class)
public abstract class FluidBlockMixin extends Block implements BucketPickup {
    public FluidBlockMixin(Properties settings) {
        super(settings);
    }

    @ModifyArg(method = "shouldSpreadLiquid", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;setBlockAndUpdate(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Z", ordinal = 0), index = 1)
    private BlockState blockStateModify(BlockState state, @Local(argsOnly = true) Level world, @Local(argsOnly = true) BlockPos blockPos) {

        if (!ModConfigs.deepslate_generator)
            return state;

        if (blockPos.getY() > 8)
            return state;
        else {
            if (world.getFluidState(blockPos).isSource())
                return state;
            else
                return Blocks.COBBLED_DEEPSLATE.defaultBlockState();
        }
    }
}