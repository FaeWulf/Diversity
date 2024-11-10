package xyz.faewulf.diversity.mixin.bonemealCoral;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.BaseCoralPlantTypeBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CoralFanBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.CoralClawFeature;
import net.minecraft.world.level.levelgen.feature.CoralFeature;
import net.minecraft.world.level.levelgen.feature.CoralMushroomFeature;
import net.minecraft.world.level.levelgen.feature.CoralTreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.MapColor;
import org.spongepowered.asm.mixin.Mixin;
import xyz.faewulf.diversity.inter.ICustomBonemealable;
import xyz.faewulf.diversity.util.compare;

import java.util.Optional;

import static net.minecraft.world.level.block.Block.UPDATE_ALL;

@Mixin(CoralFanBlock.class)
public class CoralFanMixin implements ICustomBonemealable {

    @Override
    public boolean Diversity$isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        Holder<Biome> currentBiome = level.getBiome(pos);
        return
                !compare.isHasTag(state.getBlock(), "diversity:bonemeal_blacklist")
                        && state.getValue(BaseCoralPlantTypeBlock.WATERLOGGED)
                        && level.getFluidState(pos.above()).is(FluidTags.WATER)
                        && currentBiome.is(BiomeTags.PRODUCES_CORALS_FROM_BONEMEAL);
    }

    @Override
    public boolean Diversity$isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return level.random.nextFloat() < 0.12D;
    }

    @Override
    public void Diversity$performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        CoralFeature coral = switch (level.random.nextInt(3)) {
            case 0 -> new CoralTreeFeature(NoneFeatureConfiguration.CODEC);
            case 1 -> new CoralClawFeature(NoneFeatureConfiguration.CODEC);
            default -> new CoralMushroomFeature(NoneFeatureConfiguration.CODEC);
        };

        //get corresponding block based on the fan, use color map...
        MapColor color = state.getMapColor(level, pos);
        BlockState coralBlockMatchedColor = state;
        //get all coral blocks
        Optional<HolderSet.Named<Block>> coralBlocksRegistry = level.registryAccess().lookupOrThrow(Registries.BLOCK).get(BlockTags.CORAL_BLOCKS);
        HolderSet.Named<Block> coralBlocks;

        //extra check
        if (coralBlocksRegistry.isPresent())
            coralBlocks = coralBlocksRegistry.get();
        else {
            return;
        }

        //then find using color
        for (Holder<Block> coral_block : coralBlocks) {
            coralBlockMatchedColor = coral_block.value().defaultBlockState();
            if (coralBlockMatchedColor.getMapColor(level, pos) == color) {
                break;
            }
        }

        level.setBlock(pos, Blocks.WATER.defaultBlockState(), Block.UPDATE_NONE);

        if (!((CoralFeatureInvoker) coral).generateCoral(level, level.random, pos, coralBlockMatchedColor)) {
            level.setBlock(pos, state, UPDATE_ALL);
        }


        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER, // Bonemeal-like particles
                    pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, // Position (above the block)
                    7, // Number of particles
                    0.3, 0.3, 0.3, // Particle spread on X, Y, Z axes
                    0.1 // Particle speed
            );
        }
    }
}
