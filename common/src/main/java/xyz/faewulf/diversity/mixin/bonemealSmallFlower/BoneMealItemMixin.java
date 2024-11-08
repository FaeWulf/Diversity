package xyz.faewulf.diversity.mixin.bonemealSmallFlower;

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
import xyz.faewulf.diversity.mixin.bonemealCoral.CoralFeatureInvoker;
import xyz.faewulf.diversity.util.compare;
import xyz.faewulf.diversity.util.config.ModConfigs;

import java.util.Optional;

import static net.minecraft.world.level.block.Block.UPDATE_ALL;

@Mixin(BoneMealItem.class)
public class BoneMealItemMixin {

    @Inject(method = "growCrop", at = @At(value = "HEAD"), cancellable = true)
    private static void growPlantInject(ItemStack stack, Level level, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {

        if (!ModConfigs.bonemeal_small_flower)
            return;

        BlockState block = level.getBlockState(pos);

        //check valid tag
        if (block.is(BlockTags.SMALL_FLOWERS)
                && !compare.isHasTag(block.getBlock(), "diversity:bonemeal_blacklist")
                && compare.isHasTag(level.getBlockState(pos.below()).getBlock(), "diversity:rich_soil")
                && level instanceof ServerLevel serverLevel
        ) {

            //chance to fail bonemeal
            if (level.random.nextFloat() >= 0.5D) {
                serverLevel.playSound(null, pos, SoundEvents.BONE_MEAL_USE, SoundSource.PLAYERS, 1.0F, (1.0F + level.getRandom().nextFloat() * 0.2F) * 0.7F);
                serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER, // Bonemeal-like particles
                        pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, // Position (above the block)
                        7, // Number of particles
                        0.3, 0.3, 0.3, // Particle spread on X, Y, Z axes
                        0.1 // Particle speed
                );

                stack.shrink(1);
                cir.setReturnValue(true);
                cir.cancel();
                return;
            }

            //perform bonmeal action
            int j = 1;
            int l = 0;
            int m = pos.getX() - 1;
            int n = 0;

            for (int o = 0; o < 3; ++o) {
                for (int p = 0; p < j; ++p) {
                    int q = 2 + pos.getY() - 1;

                    for (int r = q - 2; r < q; ++r) {
                        BlockPos blockPos = new BlockPos(m + o, r, pos.getZ() - n + p);
                        if (!blockPos.equals(pos) && level.random.nextInt(12) == 0 && level.getBlockState(blockPos).is(Blocks.AIR)) {
                            BlockState blockState = level.getBlockState(blockPos.below());
                            if (compare.isHasTag(blockState.getBlock(), "diversity:rich_soil")) {
                                level.setBlock(blockPos, block, Block.UPDATE_ALL);
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

            serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER, // Bonemeal-like particles
                    pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, // Position (above the block)
                    7, // Number of particles
                    0.3, 0.3, 0.3, // Particle spread on X, Y, Z axes
                    0.1 // Particle speed
            );
            serverLevel.playSound(null, pos, SoundEvents.BONE_MEAL_USE, SoundSource.PLAYERS, 1.0F, (1.0F + serverLevel.getRandom().nextFloat() * 0.2F) * 0.7F);

            stack.shrink(1);
            cir.setReturnValue(true);
            cir.cancel();
        }
    }
}
