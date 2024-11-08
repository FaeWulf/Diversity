package xyz.faewulf.diversity.mixin.bonemealCoral;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
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
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.faewulf.diversity.util.compare;
import xyz.faewulf.diversity.util.config.ModConfigs;

import java.util.Optional;

import static net.minecraft.world.level.block.Block.UPDATE_ALL;

@Mixin(BoneMealItem.class)
public class BoneMealItemMixin {

    @Inject(method = "growCrop", at = @At(value = "HEAD"), cancellable = true)
    private static void growPlantInject(ItemStack stack, Level level, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {

        if (!ModConfigs.bonemeal_coral_fan)
            return;

        BlockState block = level.getBlockState(pos);
        Holder<Biome> currentBiome = level.getBiome(pos);

        if (block.is(BlockTags.CORALS)
                && !compare.isHasTag(block.getBlock(), "diversity:bonemeal_blacklist")
                && block.getValue(BaseCoralPlantTypeBlock.WATERLOGGED)
                && level.getFluidState(pos.above()).is(FluidTags.WATER)
                && currentBiome.is(BiomeTags.PRODUCES_CORALS_FROM_BONEMEAL)
        ) {
            if (level.random.nextFloat() >= 0.15D) {

                if (level instanceof ServerLevel serverLevel) {
                    serverLevel.playSound(null, pos, SoundEvents.BONE_MEAL_USE, SoundSource.BLOCKS, 1.0F, (1.0F + level.getRandom().nextFloat() * 0.2F) * 0.7F);
                    serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER, // Bonemeal-like particles
                            pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, // Position (above the block)
                            7, // Number of particles
                            0.3, 0.3, 0.3, // Particle spread on X, Y, Z axes
                            0.1 // Particle speed
                    );
                }

                stack.shrink(1);
                cir.setReturnValue(true);
                cir.cancel();
                return;
            }


            //perform bonmeal action

            CoralFeature coral = switch (level.random.nextInt(3)) {
                case 0 -> new CoralTreeFeature(NoneFeatureConfiguration.CODEC);
                case 1 -> new CoralClawFeature(NoneFeatureConfiguration.CODEC);
                default -> new CoralMushroomFeature(NoneFeatureConfiguration.CODEC);
            };

            //get corresponding block based on the fan, use color map...
            MapColor color = block.getMapColor(level, pos);
            BlockState coralBlockMatchedColor = block;
            //get all coral blocks
            Optional<HolderSet.Named<Block>> coralBlocksRegistry = level.registryAccess().lookupOrThrow(Registries.BLOCK).get(BlockTags.CORAL_BLOCKS);
            HolderSet.Named<Block> coralBlocks;

            //extra check
            if (coralBlocksRegistry.isPresent())
                coralBlocks = coralBlocksRegistry.get();
            else {
                cir.setReturnValue(false);
                cir.cancel();
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
                level.setBlock(pos, block, UPDATE_ALL);
            }


            if (level instanceof ServerLevel serverLevel) {
                serverLevel.playSound(null, pos, SoundEvents.BONE_MEAL_USE, SoundSource.PLAYERS, 1.0F, (1.0F + serverLevel.getRandom().nextFloat() * 0.2F) * 0.7F);
                serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER, // Bonemeal-like particles
                        pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, // Position (above the block)
                        7, // Number of particles
                        0.3, 0.3, 0.3, // Particle spread on X, Y, Z axes
                        0.1 // Particle speed
                );
            }

            stack.shrink(1);
            cir.setReturnValue(true);
            cir.cancel();
        }
    }
}
