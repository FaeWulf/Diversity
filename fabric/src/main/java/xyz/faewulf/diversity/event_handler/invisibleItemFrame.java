package xyz.faewulf.diversity.event_handler;

import net.fabricmc.fabric.api.event.player.UseEntityCallback;

public class invisibleItemFrame {
    public static void register() {
        UseEntityCallback.EVENT.register(((player, world, hand, entity, hitResult) -> xyz.faewulf.diversity.event.invisibleItemFrame.run(world, player, hand, entity, hitResult)));
    }

}
