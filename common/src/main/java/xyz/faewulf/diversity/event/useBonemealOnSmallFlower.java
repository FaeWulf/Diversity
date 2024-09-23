package xyz.faewulf.diversity.event;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import xyz.faewulf.diversity.util.compare;
import xyz.faewulf.diversity.util.config.ModConfigs;

public class useBonemealOnSmallFlower {
    public static InteractionResult run(Level world, Player player, InteractionHand hand, BlockHitResult hitResult) {

        if (!ModConfigs.bonemeal_small_flower)
            return InteractionResult.PASS;

        if (world.isClientSide)
            return InteractionResult.PASS;

        Item item = player.getItemInHand(hand).getItem();

        if (item == Items.BONE_MEAL) {

            BlockState block = world.getBlockState(hitResult.getBlockPos());
            BlockPos pos = hitResult.getBlockPos();

            //check valid tag
            if (block.is(BlockTags.SMALL_FLOWERS)
                    && !compare.isHasTag(block.getBlock(), "diversity:bonemeal_blacklist")
                    && compare.isHasTag(world.getBlockState(pos.below()).getBlock(), "diversity:rich_soil")
            ) {

                //chance to fail bonemeal
                if (world.random.nextFloat() >= 0.5D) {
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
                int j = 1;
                int l = 0;
                int m = pos.getX() - 1;
                int n = 0;

                for (int o = 0; o < 3; ++o) {
                    for (int p = 0; p < j; ++p) {
                        int q = 2 + pos.getY() - 1;

                        for (int r = q - 2; r < q; ++r) {
                            BlockPos blockPos = new BlockPos(m + o, r, pos.getZ() - n + p);
                            if (!blockPos.equals(pos) && world.random.nextInt(12) == 0 && world.getBlockState(blockPos).is(Blocks.AIR)) {
                                BlockState blockState = world.getBlockState(blockPos.below());
                                if (compare.isHasTag(blockState.getBlock(), "diversity:rich_soil")) {
                                    world.setBlock(blockPos, block, Block.UPDATE_ALL);
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

                player.swing(hand, true);
                if (world instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER, // Bonemeal-like particles
                            pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, // Position (above the block)
                            7, // Number of particles
                            0.3, 0.3, 0.3, // Particle spread on X, Y, Z axes
                            0.1 // Particle speed
                    );
                    serverLevel.playSound(null, pos, SoundEvents.BONE_MEAL_USE, SoundSource.PLAYERS, 1.0F, (1.0F + world.getRandom().nextFloat() * 0.2F) * 0.7F);
                }
                player.getItemInHand(hand).consume(1, player);
                return InteractionResult.CONSUME;
            }
        }

        return InteractionResult.PASS;
    }
}
