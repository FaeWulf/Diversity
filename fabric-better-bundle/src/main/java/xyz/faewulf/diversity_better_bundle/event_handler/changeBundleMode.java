package xyz.faewulf.diversity_better_bundle.event_handler;

import net.fabricmc.fabric.api.event.player.AttackBlockCallback;

public class changeBundleMode {
    static public void register() {
        AttackBlockCallback.EVENT.register((player, world, hand, pos, direction) -> xyz.faewulf.diversity_better_bundle.event.changeBundleMode.run(world, player, hand, pos, direction));
    }
}
