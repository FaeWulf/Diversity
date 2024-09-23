package xyz.faewulf.diversity.event_handler;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;

public class useBonemealOnCoral {
    public static void register() {
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> xyz.faewulf.diversity.event.useBonemealOnCoral.run(world, player, hand, hitResult));
    }
}
