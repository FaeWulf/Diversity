package xyz.faewulf.diversity.event_handler;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;

public class onRightClickCropBlocks {
    public static void register() {
        UseBlockCallback.EVENT.register(((player, world, hand, hitResult) -> xyz.faewulf.diversity.event.rightClickCropBlocks.run(world, player, hand, hitResult)));
    }
}
