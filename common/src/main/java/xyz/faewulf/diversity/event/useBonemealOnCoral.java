package xyz.faewulf.diversity.event;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.BaseCoralPlantTypeBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.CoralClawFeature;
import net.minecraft.world.level.levelgen.feature.CoralFeature;
import net.minecraft.world.level.levelgen.feature.CoralMushroomFeature;
import net.minecraft.world.level.levelgen.feature.CoralTreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import xyz.faewulf.diversity.mixin.bonemealCoral.CoralFeatureInvoker;
import xyz.faewulf.diversity.util.compare;
import xyz.faewulf.diversity.util.config.ModConfigs;

import java.util.Optional;

import static net.minecraft.world.level.block.Block.UPDATE_ALL;

public class useBonemealOnCoral {
    public static InteractionResult run(Level world, Player player, InteractionHand hand, BlockHitResult hitResult) {

        if (!ModConfigs.bonemeal_coral_fan)
            return InteractionResult.PASS;

        if (world.isClientSide)
            return InteractionResult.PASS;

        Item item = player.getItemInHand(hand).getItem();

        if (item == Items.BONE_MEAL) {

            BlockState block = world.getBlockState(hitResult.getBlockPos());
            BlockPos pos = hitResult.getBlockPos();

            ResourceKey<Biome> currentBiome;

            if (world.getBiome(pos).unwrapKey().isPresent()) {
                currentBiome = world.getBiome(pos).unwrapKey().get();
            } else
                return InteractionResult.PASS;

            //check valid criteria
            if (block.is(BlockTags.CORALS)
                    && !compare.isHasTag(block.getBlock(), "diversity:bonemeal_blacklist")
                    && block.getValue(BaseCoralPlantTypeBlock.WATERLOGGED)
                    && world.getFluidState(pos.above()).is(FluidTags.WATER)
                    && currentBiome.equals(Biomes.WARM_OCEAN)
            ) {

                if (world.random.nextFloat() >= 0.15D) {
                    player.swing(hand, true);

                    if (world instanceof ServerLevel serverLevel) {
                        serverLevel.playSound(null, pos, SoundEvents.BONE_MEAL_USE, SoundSource.PLAYERS, 1.0F, (1.0F + world.getRandom().nextFloat() * 0.2F) * 0.7F);
                        serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER, // Bonemeal-like particles
                                pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, // Position (above the block)
                                7, // Number of particles
                                0.3, 0.3, 0.3, // Particle spread on X, Y, Z axes
                                0.1 // Particle speed
                        );
                    }

                    return InteractionResult.PASS;
                }


                //perform bonmeal action

                CoralFeature coral = switch (world.random.nextInt(3)) {
                    case 0 -> new CoralTreeFeature(NoneFeatureConfiguration.CODEC);
                    case 1 -> new CoralClawFeature(NoneFeatureConfiguration.CODEC);
                    default -> new CoralMushroomFeature(NoneFeatureConfiguration.CODEC);
                };

                //get corresponding block based on the fan, use color map...
                MapColor color = block.getMapColor(world, pos);
                BlockState coralBlockMatchedColor = block;
                //get all coral blocks
                Optional<HolderSet.Named<Block>> coralBlocksRegistry = world.registryAccess().registryOrThrow(Registries.BLOCK).getTag(BlockTags.CORAL_BLOCKS);
                HolderSet.Named<Block> coralBlocks;

                //extra check
                if (coralBlocksRegistry.isPresent())
                    coralBlocks = coralBlocksRegistry.get();
                else
                    return InteractionResult.PASS;

                //then find using color
                for (Holder<Block> coral_block : coralBlocks) {
                    coralBlockMatchedColor = coral_block.value().defaultBlockState();
                    if (coralBlockMatchedColor.getMapColor(world, pos) == color) {
                        break;
                    }
                }

                world.setBlock(pos, Blocks.WATER.defaultBlockState(), Block.UPDATE_NONE);

                if (!((CoralFeatureInvoker) coral).generateCoral(world, world.random, pos, coralBlockMatchedColor)) {
                    world.setBlock(pos, block, UPDATE_ALL);
                }


                //handle perform action/effect
                player.swing(hand, true);

                if (world instanceof ServerLevel serverLevel) {
                    serverLevel.playSound(null, pos, SoundEvents.BONE_MEAL_USE, SoundSource.PLAYERS, 1.0F, (1.0F + world.getRandom().nextFloat() * 0.2F) * 0.7F);
                    serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER, // Bonemeal-like particles
                            pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, // Position (above the block)
                            7, // Number of particles
                            0.3, 0.3, 0.3, // Particle spread on X, Y, Z axes
                            0.1 // Particle speed
                    );
                }

                player.getItemInHand(hand).consume(1, player);
                return InteractionResult.CONSUME;
            }
        }

        return InteractionResult.PASS;
    }
}
