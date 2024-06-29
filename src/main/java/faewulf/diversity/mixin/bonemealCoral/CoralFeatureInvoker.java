package faewulf.diversity.mixin.bonemealCoral;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.CoralFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(CoralFeature.class)
public abstract interface CoralFeatureInvoker {
    @Invoker("generateCoral")
    public abstract boolean generateCoral(WorldAccess world, Random random, BlockPos pos, BlockState state);
}
