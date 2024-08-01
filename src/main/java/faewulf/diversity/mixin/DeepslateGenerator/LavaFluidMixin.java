package faewulf.diversity.mixin.DeepslateGenerator;

import com.llamalad7.mixinextras.sugar.Local;
import faewulf.diversity.util.ModConfigs;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.LavaFluid;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LavaFluid.class)
public abstract class LavaFluidMixin extends FlowableFluid {

    @ModifyArg(method = "flow", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/WorldAccess;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"), index = 1)
    private BlockState modifyBlockState(BlockState par2, @Local(argsOnly = true) BlockPos blockPos) {

        if (!ModConfigs.deepslate_generator)
            return par2;

        if (blockPos.getY() > 8)
            return par2;
        else
            return Blocks.DEEPSLATE.getDefaultState();
    }
}
