package faewulf.diversity.mixin.DeepslateGenerator;

import com.llamalad7.mixinextras.sugar.Local;
import faewulf.diversity.util.ModConfigs;
import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(FluidBlock.class)
public abstract class FluidBlockMixin extends Block implements FluidDrainable {
    public FluidBlockMixin(Settings settings) {
        super(settings);
    }

    @ModifyArg(method = "receiveNeighborFluids", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)Z", ordinal = 0), index = 1)
    private BlockState blockStateModify(BlockState state, @Local(argsOnly = true) BlockPos blockPos) {

        if (!ModConfigs.deepslate_generator)
            return state;

        if (blockPos.getY() > 8)
            return state;
        else
            return Blocks.COBBLED_DEEPSLATE.getDefaultState();
    }
}