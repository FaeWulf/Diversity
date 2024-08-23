package xyz.faewulf.diversity.inter;

import xyz.faewulf.diversity.mixin.bonemealCoral.CoralFeatureInvoker;
import xyz.faewulf.diversity.util.ModConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.BaseCoralPlantTypeBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.CoralClawFeature;
import net.minecraft.world.level.levelgen.feature.CoralFeature;
import net.minecraft.world.level.levelgen.feature.CoralMushroomFeature;
import net.minecraft.world.level.levelgen.feature.CoralTreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.MapColor;

import java.util.Optional;

import static net.minecraft.world.level.block.Block.UPDATE_ALL;

public interface ICustomFertilizeCoral extends BonemealableBlock {
    @Override
    public default boolean isValidBonemealTarget(LevelReader world, BlockPos pos, BlockState state) {

        if (!ModConfigs.bonemeal_coral_fan)
            return false;

        ResourceKey<Biome> currentBiome;

        if (world.getBiome(pos).unwrapKey().isPresent()) {
            currentBiome = world.getBiome(pos).unwrapKey().get();
        } else
            return false;

        return state.getValue(BaseCoralPlantTypeBlock.WATERLOGGED)
                && world.getFluidState(pos.above()).is(FluidTags.WATER)
                && currentBiome.equals(Biomes.WARM_OCEAN);
    }

    @Override
    public default boolean isBonemealSuccess(Level world, RandomSource random, BlockPos pos, BlockState state) {
        return random.nextFloat() < 0.15D;
    }

    @Override
    public default void performBonemeal(ServerLevel world, RandomSource random, BlockPos pos, BlockState state) {

        CoralFeature coral = switch (random.nextInt(3)) {
            case 0 -> new CoralTreeFeature(NoneFeatureConfiguration.CODEC);
            case 1 -> new CoralClawFeature(NoneFeatureConfiguration.CODEC);
            default -> new CoralMushroomFeature(NoneFeatureConfiguration.CODEC);
        };

        //get corresponding block based on the fan, use color map...
        MapColor color = state.getMapColor(world, pos);
        BlockState coralBlockMatchedColor = state;
        //get all coral blocks
        Optional<HolderSet.Named<Block>> coralBlocksRegistry = world.registryAccess().registryOrThrow(Registries.BLOCK).getTag(BlockTags.CORAL_BLOCKS);
        HolderSet.Named<Block> coralBlocks;

        //extra check
        if (coralBlocksRegistry.isPresent())
            coralBlocks = coralBlocksRegistry.get();
        else
            return;

        //then find using color
        for (Holder<Block> block : coralBlocks) {
            coralBlockMatchedColor = block.value().defaultBlockState();
            if (coralBlockMatchedColor.getMapColor(world, pos) == color) {
                break;
            }
        }

        world.setBlock(pos, Blocks.WATER.defaultBlockState(), Block.UPDATE_NONE);

        if (!((CoralFeatureInvoker) coral).generateCoral(world, random, pos, coralBlockMatchedColor)) {
            world.setBlock(pos, state, UPDATE_ALL);
        }
    }
}
