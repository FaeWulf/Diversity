package xyz.faewulf.diversity.event;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CocoaBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.NetherWartBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import xyz.faewulf.diversity.util.MissingMethod.ItemStackMethod;
import xyz.faewulf.diversity.util.MissingMethod.LivingEntityMethod;
import xyz.faewulf.diversity.util.compare;
import xyz.faewulf.diversity.util.config.ModConfigs;

import java.util.List;

public class rightClickCropBlocks {

    public static InteractionResult run(Level level, Player player, InteractionHand hand, BlockHitResult hitResult) {

        if (!ModConfigs.hoe_harvest_crop)
            return InteractionResult.PASS;

        //first filter
        if (hand == InteractionHand.MAIN_HAND
                && hitResult.getType().equals(HitResult.Type.BLOCK)
                && level instanceof ServerLevel serverLevel
        ) {

            ItemStack mainHandItem = player.getItemInHand(InteractionHand.MAIN_HAND);
            BlockPos targetBlock = hitResult.getBlockPos();
            BlockState blockState = level.getBlockState(targetBlock);

            //is using hoe
            boolean usingHoe = compare.isHasTag(mainHandItem.getItem(), "diversity:crop_harvester") && enableRadius(blockState);

            //radius trigger
            int radius = 1;

            //if tier 2 hoe
            if (usingHoe && compare.isHasTag(mainHandItem.getItem(), "diversity:tier2_hoe"))
                radius = 2;

            //not using hoe
            if (!usingHoe)
                radius = 0;

            boolean shouldConsumeAction = false;

            //check if crops: wheat, carrot, beetroot, potato, nether wart, cocoa
            if (isCrop(blockState)) {

                //radius 3x3
                for (int dx = -radius; dx <= radius; dx++) {
                    for (int dz = -radius; dz <= radius; dz++) {
                        BlockPos currentBlock = hitResult.getBlockPos().offset(dx, 0, dz);
                        BlockState currentBlockState = level.getBlockState(currentBlock);

                        //is crops
                        if (isCrop(currentBlockState)) {
                            int age = getAge(currentBlockState);
                            int maxAge = getMaxAge(currentBlockState);

                            //harvest
                            if (age == maxAge) {

                                //get loot
                                Vec3 center = currentBlock.getCenter();
                                List<ItemStack> itemStacks = Block.getDrops(currentBlockState, serverLevel, currentBlock, null, player, mainHandItem);

                                //drops xp
                                if (ModConfigs.xp_crops)
                                    ExperienceOrb.award(serverLevel, center, level.random.nextIntBetweenInclusive(0, 1));

                                //Handle check if having seed in loot, or not then use seed in player inventory
                                boolean alreadyTakeSeed = false;
                                for (ItemStack itemStack : itemStacks) {
                                    //check if is seed, then remove 1
                                    if (!alreadyTakeSeed && isSeed(itemStack.getItem(), currentBlockState)) {
                                        alreadyTakeSeed = true;
                                        itemStack.shrink(1);
                                    }

                                    //check if empty
                                    if (itemStack.getCount() == 0)
                                        continue;

                                    //drop loot
                                    ItemEntity item = new ItemEntity(serverLevel, center.x, center.y, center.z, itemStack);
                                    item.setDefaultPickUpDelay();
                                    serverLevel.addFreshEntity(item);
                                }

                                //if doesn't drop seed then check in player inv
                                if (!alreadyTakeSeed) {
                                    for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                                        ItemStack stack = player.getInventory().getItem(i);
                                        if (!stack.isEmpty() && isSeed(stack.getItem(), currentBlockState)) {
                                            alreadyTakeSeed = true;
                                            stack.shrink(1);
                                            break;
                                        }
                                    }
                                }

                                //if taken seed then replace crop
                                if (alreadyTakeSeed)
                                    replaceCrop(serverLevel, currentBlockState, currentBlock);

                                //replica of breaking block
                                serverLevel.playSound(null, targetBlock, currentBlockState.getSoundType().getBreakSound(), SoundSource.BLOCKS, 1.0f, 1.0f);
                                serverLevel.sendParticles(
                                        new BlockParticleOption(ParticleTypes.BLOCK, currentBlockState),
                                        center.x, center.y - 0.2f, center.z,   // Particle position (centered on block)
                                        40,                             // Particle count
                                        0.25, 0.25, 0.25,  // Spread in x, y, z directions
                                        0.5
                                );
                                player.causeFoodExhaustion(0.005f);

                                //damage item
                                if (usingHoe)
                                    ItemStackMethod.hurtAndBreak(player.getItemInHand(hand), 1, player, LivingEntityMethod.getSlotForHand(hand));

                                shouldConsumeAction = true;
                            }
                        }
                    }
                }

                // end effect
                if (shouldConsumeAction) {

                    //swing hand
                    player.swing(hand, true);
                    return InteractionResult.CONSUME;
                }
            }
        }

        return InteractionResult.PASS;
    }

    private static boolean isCrop(BlockState blockState) {
        return blockState.getBlock() instanceof CropBlock
                || blockState.getBlock() instanceof NetherWartBlock
                || blockState.getBlock() instanceof CocoaBlock;
    }

    private static boolean enableRadius(BlockState blockState) {
        return blockState.getBlock() instanceof CropBlock
                || blockState.getBlock() instanceof NetherWartBlock;
    }

    private static boolean isSeed(Item target, BlockState blockState) {
        return target instanceof BlockItem blockItem && blockItem.getBlock() == blockState.getBlock();
    }

    private static int getAge(BlockState blockState) {
        //crop
        if (blockState.getBlock() instanceof CropBlock cropBlock) {
            return cropBlock.getAge(blockState);
        }

        //nether wart
        if (blockState.getBlock() instanceof NetherWartBlock) {
            return blockState.getValue(BlockStateProperties.AGE_3);
        }

        //cocoa
        if (blockState.getBlock() instanceof CocoaBlock) {
            return blockState.getValue(BlockStateProperties.AGE_2);
        }

        return -1;
    }

    private static int getMaxAge(BlockState blockState) {
        //crop
        if (blockState.getBlock() instanceof CropBlock cropBlock) {
            return cropBlock.getMaxAge();
        }

        //nether wart
        if (blockState.getBlock() instanceof NetherWartBlock) {
            return 3;
        }

        //cocoa
        if (blockState.getBlock() instanceof CocoaBlock) {
            return 2;
        }

        return 0;
    }

    private static void replaceCrop(ServerLevel level, BlockState blockState, BlockPos blockPos) {
        BlockState result = blockState.getBlock().defaultBlockState();

        if (blockState.getBlock() instanceof CocoaBlock) {
            result = result.setValue(BlockStateProperties.HORIZONTAL_FACING, blockState.getValue(BlockStateProperties.HORIZONTAL_FACING));
        }

        level.setBlock(blockPos, result, Block.UPDATE_ALL);
    }
}
