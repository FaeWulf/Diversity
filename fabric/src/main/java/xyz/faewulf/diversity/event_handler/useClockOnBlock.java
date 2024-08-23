package xyz.faewulf.diversity.event_handler;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;

public class useClockOnBlock {
    public static void register() {
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> xyz.faewulf.diversity.event.useClockOnBlock.run(world, player, hand, hitResult));
    }
}
