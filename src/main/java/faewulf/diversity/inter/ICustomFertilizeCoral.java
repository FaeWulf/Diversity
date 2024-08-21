package faewulf.diversity.inter;

import faewulf.diversity.mixin.bonemealCoral.CoralFeatureInvoker;
import faewulf.diversity.util.ModConfigs;
import net.minecraft.block.*;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.feature.*;

import java.util.Optional;

import static net.minecraft.block.Block.NOTIFY_ALL;

public interface ICustomFertilizeCoral extends Fertilizable {

    @Override
    public default boolean isFertilizable(WorldView world, BlockPos pos, BlockState state
                                          //? =1.20.1 {
            , boolean isClient
                                          //?}
    ) {

        if (!ModConfigs.bonemeal_coral_fan)
            return false;

        RegistryKey<Biome> currentBiome;

        if (world.getBiome(pos).getKey().isPresent()) {
            currentBiome = world.getBiome(pos).getKey().get();
        } else
            return false;

        return state.get(CoralParentBlock.WATERLOGGED)
                && world.getFluidState(pos.up()).isIn(FluidTags.WATER)
                && currentBiome.equals(BiomeKeys.WARM_OCEAN);
    }

    @Override
    public default boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return random.nextFloat() < 0.15D;
    }

    @Override
    public default void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {

        CoralFeature coral = switch (random.nextInt(3)) {
            case 0 -> new CoralTreeFeature(DefaultFeatureConfig.CODEC);
            case 1 -> new CoralClawFeature(DefaultFeatureConfig.CODEC);
            default -> new CoralMushroomFeature(DefaultFeatureConfig.CODEC);
        };

        //get corresponding block based on the fan, use color map...
        MapColor color = state.getMapColor(world, pos);
        BlockState coralBlockMatchedColor = state;
        //get all coral blocks
        Optional<RegistryEntryList.Named<Block>> coralBlocksRegistry = world.getRegistryManager().get(RegistryKeys.BLOCK).getEntryList(BlockTags.CORAL_BLOCKS);
        RegistryEntryList.Named<Block> coralBlocks;

        //extra check
        if (coralBlocksRegistry.isPresent())
            coralBlocks = coralBlocksRegistry.get();
        else
            return;

        //then find using color
        for (RegistryEntry<Block> block : coralBlocks) {
            coralBlockMatchedColor = block.value().getDefaultState();
            if (coralBlockMatchedColor.getMapColor(world, pos) == color) {
                break;
            }
        }

        world.setBlockState(pos, Blocks.WATER.getDefaultState(), Block.field_31035);

        if (!((CoralFeatureInvoker) coral).generateCoral(world, random, pos, coralBlockMatchedColor)) {
            world.setBlockState(pos, state, NOTIFY_ALL);
        }
    }
}
