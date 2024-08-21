package faewulf.diversity.mixin.bonemealSmallFlower;

import faewulf.diversity.util.ModConfigs;
import net.minecraft.block.*;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(FlowerBlock.class)
public abstract class PlantBlockMixin extends Block implements Fertilizable {
    public PlantBlockMixin(Settings settings) {
        super(settings);
    }

    @Override
    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state
                                  //? =1.20.1 {
            , boolean isClient
                                  //?}
    ) {

        if (!ModConfigs.bonemeal_small_flower)
            return false;

        return state.isIn(BlockTags.SMALL_FLOWERS) && state.getBlock() != Blocks.WITHER_ROSE && state.getBlock() != Blocks.TORCHFLOWER;
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {

        if (world.isClient)
            return;

        if (world.getBlockState(pos.down()).isIn(BlockTags.DIRT)) {
            int j = 1;
            int l = 0;
            int m = pos.getX() - 1;
            int n = 0;

            for (int o = 0; o < 3; ++o) {
                for (int p = 0; p < j; ++p) {
                    int q = 2 + pos.getY() - 1;

                    for (int r = q - 2; r < q; ++r) {
                        BlockPos blockPos = new BlockPos(m + o, r, pos.getZ() - n + p);
                        if (!blockPos.equals(pos) && random.nextInt(12) == 0 && world.getBlockState(blockPos).isOf(Blocks.AIR)) {
                            BlockState blockState = world.getBlockState(blockPos.down());
                            if (blockState.isIn(BlockTags.DIRT)) {
                                world.setBlockState(blockPos, state, Block.NOTIFY_ALL);
                            }
                        }
                    }
                }

                if (l < 2) {
                    j += 2;
                    ++n;
                } else {
                    j -= 2;
                    --n;
                }

                ++l;
            }
        }
    }
}
