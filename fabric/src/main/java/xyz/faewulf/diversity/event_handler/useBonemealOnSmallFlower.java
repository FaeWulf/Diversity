package xyz.faewulf.diversity.event_handler;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;

public class useBonemealOnSmallFlower {
    public static void register() {
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> xyz.faewulf.diversity.event.useBonemealOnSmallFlower.run(world, player, hand, hitResult));
    }
}
