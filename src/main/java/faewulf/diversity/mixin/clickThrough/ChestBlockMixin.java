package faewulf.diversity.mixin.clickThrough;

import net.minecraft.block.BlockState;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(net.minecraft.block.ChestBlock.class)
public interface ChestBlockMixin {
    @Invoker("createScreenHandlerFactory")
    abstract @Nullable NamedScreenHandlerFactory invokerCreateScreenHandlerFactory(BlockState state, World world, BlockPos pos);
}
