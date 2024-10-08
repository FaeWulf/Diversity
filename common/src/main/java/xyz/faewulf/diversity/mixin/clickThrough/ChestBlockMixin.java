package xyz.faewulf.diversity.mixin.clickThrough;

import net.minecraft.core.BlockPos;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(net.minecraft.world.level.block.ChestBlock.class)
public interface ChestBlockMixin {
    @Invoker("getMenuProvider")
    @Nullable MenuProvider invokerCreateScreenHandlerFactory(BlockState state, Level world, BlockPos pos);
}
