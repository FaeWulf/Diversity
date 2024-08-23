package xyz.faewulf.diversity.event_handler;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;

public class breakCrops {
    public static void register() {
        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
            xyz.faewulf.diversity.event.breakCrops.run(world, player, pos, state);
        });
    }
}
