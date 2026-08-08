package xyz.faewulf.diversity.event_handler;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import xyz.faewulf.diversity.event.xpCrops;

public class breakCrops {
    public static void register() {
        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
            xpCrops.run(world, player, pos, state);
        });
    }
}
