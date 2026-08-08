package xyz.faewulf.diversity.event_handler;

import xyz.faewulf.diversity.callback.BlockPlacedCallback;

public class placeWetSpongeBlock {

    public static void register() {
        BlockPlacedCallback.EVENT.register(context -> {
            xyz.faewulf.diversity.event.placeWetSpongeBlock.run(context.getLevel(), context.getPlayer(), context.getClickedPos());
        });
    }
}
