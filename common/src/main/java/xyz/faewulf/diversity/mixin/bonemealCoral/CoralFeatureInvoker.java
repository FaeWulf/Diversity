package xyz.faewulf.diversity.mixin.bonemealCoral;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.CoralFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(CoralFeature.class)
public abstract interface CoralFeatureInvoker {
    @Invoker("placeFeature")
    public abstract boolean generateCoral(LevelAccessor world, RandomSource random, BlockPos pos, BlockState state);
}
