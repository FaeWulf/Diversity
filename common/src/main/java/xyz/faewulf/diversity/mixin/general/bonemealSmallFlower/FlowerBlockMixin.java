package xyz.faewulf.diversity.mixin.general.bonemealSmallFlower;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import xyz.faewulf.diversity.inter.ICustomBonemealable;
import xyz.faewulf.diversity.util.compare;
import xyz.faewulf.diversity.util.config.ModConfigs;

@Mixin(FlowerBlock.class)
public class FlowerBlockMixin implements ICustomBonemealable {
    @Override
    public boolean Diversity$isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {

        if (!ModConfigs.bonemeal_small_flower)
            return false;

        //small flower
        //not in blacklist tag
        //standing on block with rich_soil tag
        return state.is(BlockTags.SMALL_FLOWERS)
                && !compare.isHasTag(state.getBlock(), "diversity:bonemeal_blacklist")
                && compare.isHasTag(level.getBlockState(pos.below()).getBlock(), "diversity:rich_soil");
    }

    @Override
    public boolean Diversity$isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return random.nextFloat() < 0.5D;
    }

    @Override
    public void Diversity$performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {

        int j = 1;
        int l = 0;
        int m = pos.getX() - 1;
        int n = 0;

        for (int o = 0; o < 3; ++o) {
            for (int p = 0; p < j; ++p) {
                int q = 2 + pos.getY() - 1;

                for (int r = q - 2; r < q; ++r) {
                    BlockPos blockPos = new BlockPos(m + o, r, pos.getZ() - n + p);
                    if (!blockPos.equals(pos) && random.nextInt(12) == 0 && level.getBlockState(blockPos).is(Blocks.AIR)) {
                        BlockState blockState = level.getBlockState(blockPos.below());
                        if (compare.isHasTag(blockState.getBlock(), "diversity:rich_soil")) {
                            level.setBlock(blockPos, state, Block.UPDATE_ALL);
                        }

                        level.sendParticles(ParticleTypes.HAPPY_VILLAGER, // Bonemeal-like particles
                                blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, // Position (above the block)
                                7, // Number of particles
                                0.3, 0.3, 0.3, // Particle spread on X, Y, Z axes
                                0.1 // Particle speed
                        );
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

        level.sendParticles(ParticleTypes.HAPPY_VILLAGER, // Bonemeal-like particles
                pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, // Position (above the block)
                7, // Number of particles
                0.3, 0.3, 0.3, // Particle spread on X, Y, Z axes
                0.1 // Particle speed
        );
    }
}
