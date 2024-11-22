package xyz.faewulf.diversity.event_handler;

import net.fabricmc.fabric.api.event.player.AttackBlockCallback;

public class changeBundleMode {
    static public void register() {
        AttackBlockCallback.EVENT.register((player, world, hand, pos, direction) -> xyz.faewulf.diversity.event.changeBundleMode.run(world, player, hand, pos, direction));
    }
}
