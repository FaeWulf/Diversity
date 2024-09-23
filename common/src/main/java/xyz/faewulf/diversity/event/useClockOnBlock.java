package xyz.faewulf.diversity.event;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import xyz.faewulf.diversity.feature.entity.PseudoBlockEntity.PseudoBlockEntities;
import xyz.faewulf.diversity.feature.entity.PseudoBlockEntity.PseudoBlockEntity;
import xyz.faewulf.diversity.util.config.ModConfigs;
import xyz.faewulf.diversity.util.converter;

import java.util.ArrayList;
import java.util.List;

public class useClockOnBlock {
    public static InteractionResult run(Level world, Player player, InteractionHand hand, BlockHitResult hitResult) {


        if (!ModConfigs.check_villager_schedule)
            return InteractionResult.PASS;

        if (world.isClientSide)
            return InteractionResult.PASS;

        Item item = player.getItemInHand(hand).getItem();

        if (item == Items.CLOCK) {

            final long worldTime = world.getDayTime();
            BlockState block = world.getBlockState(hitResult.getBlockPos());

            List<Block> workStations = new ArrayList<>();
            for (PoiType poiType : BuiltInRegistries.POINT_OF_INTEREST_TYPE) {
                for (BlockState blockState : poiType.matchingStates()) {
                    workStations.add(blockState.getBlock());
                }
            }

            if (block.getBlock() == Blocks.SUGAR_CANE) {
                Display display = PseudoBlockEntities.STOP_GROW.build(world);

                BlockPos pos = hitResult.getBlockPos();
                display.setPos(pos.getCenter().x, pos.getCenter().y, pos.getCenter().z);

                if (display instanceof PseudoBlockEntity pseudoBlockEntity) {
                    if (!pseudoBlockEntity.isBlockEntityAlreadyExist())
                        world.addFreshEntity(display);
                }

            }

            if (workStations.contains(block.getBlock())) {

                Component current;
                Component next;
                long timeLeft = -1;

                if (worldTime >= 12000 || worldTime < 10) {
                    current = Component.literal("Sleep").withStyle(ChatFormatting.DARK_BLUE);
                    next = Component.literal("Wander").withStyle(ChatFormatting.LIGHT_PURPLE);
                } else if (worldTime < 2000) {
                    current = Component.literal("Wander").withStyle(ChatFormatting.LIGHT_PURPLE);
                    next = Component.literal("Work").withStyle(ChatFormatting.GREEN);
                    timeLeft = 2000 - worldTime;
                } else if (worldTime < 9000) {
                    current = Component.literal("Work").withStyle(ChatFormatting.GREEN);
                    next = Component.literal("Gather").withStyle(ChatFormatting.GOLD);
                    timeLeft = 9000 - worldTime;
                } else if (worldTime < 11000) {
                    current = Component.literal("Gather").withStyle(ChatFormatting.GOLD);
                    next = Component.literal("Wander").withStyle(ChatFormatting.LIGHT_PURPLE);
                    timeLeft = 11000 - worldTime;
                } else {
                    current = Component.literal("Wander").withStyle(ChatFormatting.LIGHT_PURPLE);
                    next = Component.literal("Sleep").withStyle(ChatFormatting.DARK_BLUE);
                    timeLeft = 12000 - worldTime;
                }


                MutableComponent feedBack = Component.literal("Current: ")
                        .append(current);

                if (timeLeft != -1)
                    feedBack.append(" | Time left: ")
                            .append(Component.literal(converter.tick2Time(timeLeft)));

                feedBack.append(" | Next: ")
                        .append(next);

                player.swing(hand, true);
                player.displayClientMessage(feedBack, true);
                return InteractionResult.CONSUME;
            }
        }

        return InteractionResult.PASS;

    }
}
