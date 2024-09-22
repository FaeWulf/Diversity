package xyz.faewulf.diversity.mixin.bonemealSmallFlower;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import xyz.faewulf.diversity.util.compare;
import xyz.faewulf.diversity.util.config.ModConfigs;

@Mixin(FlowerBlock.class)
public abstract class PlantBlockMixin extends Block implements BonemealableBlock {
    public PlantBlockMixin(Properties settings) {
        super(settings);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader world, BlockPos pos, BlockState state) {

        if (!ModConfigs.bonemeal_small_flower)
            return false;

        return state.is(BlockTags.SMALL_FLOWERS) && !compare.isHasTag(state.getBlock(), "diversity:bonemeal_blacklist");
    }

    @Override
    public boolean isBonemealSuccess(Level world, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel world, RandomSource random, BlockPos pos, BlockState state) {

        if (world.isClientSide)
            return;

        if (world.getBlockState(pos.below()).is(BlockTags.DIRT)) {
            int j = 1;
            int l = 0;
            int m = pos.getX() - 1;
            int n = 0;

            for (int o = 0; o < 3; ++o) {
                for (int p = 0; p < j; ++p) {
                    int q = 2 + pos.getY() - 1;

                    for (int r = q - 2; r < q; ++r) {
                        BlockPos blockPos = new BlockPos(m + o, r, pos.getZ() - n + p);
                        if (!blockPos.equals(pos) && random.nextInt(12) == 0 && world.getBlockState(blockPos).is(Blocks.AIR)) {
                            BlockState blockState = world.getBlockState(blockPos.below());
                            if (blockState.is(BlockTags.DIRT)) {
                                world.setBlock(blockPos, state, Block.UPDATE_ALL);
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
