package xyz.faewulf.diversity.mixin.deepslateGenerator;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.LavaFluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import xyz.faewulf.diversity.util.config.ModConfigs;

@Mixin(LavaFluid.class)
public abstract class LavaFluidMixin extends FlowingFluid {

    @ModifyArg(method = "spreadTo", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/LevelAccessor;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"), index = 1)
    private BlockState modifyBlockState(BlockState par2, @Local(argsOnly = true) BlockPos blockPos) {

        if (!ModConfigs.deepslate_generator)
            return par2;

        if (blockPos.getY() > 8)
            return par2;
        else
            return Blocks.DEEPSLATE.defaultBlockState();
    }
}
