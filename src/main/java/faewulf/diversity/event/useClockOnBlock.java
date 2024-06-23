package faewulf.diversity.event;

import faewulf.diversity.util.ModConfigs;
import faewulf.diversity.util.converter;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.world.poi.PointOfInterestType;

import java.util.ArrayList;
import java.util.List;

public class useClockOnBlock {
    public static void run() {


        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {

            if (!ModConfigs.check_villager_schedule)
                return ActionResult.PASS;

            if (world.isClient)
                return ActionResult.PASS;

            Item item = player.getStackInHand(hand).getItem();

            if (item == Items.CLOCK) {

                final long worldTime = world.getTimeOfDay();
                BlockState block = world.getBlockState(hitResult.getBlockPos());

                Text current;
                Text next;
                long timeLeft = -1;

                if (worldTime >= 12000 || worldTime < 10) {
                    current = Text.literal("Sleep").formatted(Formatting.DARK_BLUE);
                    next = Text.literal("Wander").formatted(Formatting.LIGHT_PURPLE);
                } else if (worldTime < 2000) {
                    current = Text.literal("Wander").formatted(Formatting.LIGHT_PURPLE);
                    next = Text.literal("Work").formatted(Formatting.GREEN);
                    timeLeft = 2000 - worldTime;
                } else if (worldTime < 9000) {
                    current = Text.literal("Work").formatted(Formatting.GREEN);
                    next = Text.literal("Gather").formatted(Formatting.GOLD);
                    timeLeft = 9000 - worldTime;
                } else if (worldTime < 11000) {
                    current = Text.literal("Gather").formatted(Formatting.GOLD);
                    next = Text.literal("Wander").formatted(Formatting.LIGHT_PURPLE);
                    timeLeft = 11000 - worldTime;
                } else {
                    current = Text.literal("Wander").formatted(Formatting.LIGHT_PURPLE);
                    next = Text.literal("Sleep").formatted(Formatting.DARK_BLUE);
                    timeLeft = 12000 - worldTime;
                }

                List<Block> workStations = new ArrayList<>();
                for (PointOfInterestType poiType : Registries.POINT_OF_INTEREST_TYPE) {
                    for (BlockState blockState : poiType.blockStates()) {
                        workStations.add(blockState.getBlock());
                    }
                }

                if (workStations.contains(block.getBlock())) {

                    MutableText feedBack = Text.literal("Current: ")
                            .append(current);

                    if (timeLeft != -1)
                        feedBack.append(" | Time left: ")
                                .append(Text.literal(converter.tiok2Time(timeLeft)));

                    feedBack.append(" | Next: ")
                            .append(next);

                    player.sendMessage(feedBack, true);
                    return ActionResult.PASS;
                }
            }

            return ActionResult.PASS;
        });

    }
}
