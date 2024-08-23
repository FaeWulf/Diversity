package xyz.faewulf.diversity.event_handler;

import xyz.faewulf.diversity.callback.BlockPlacedCallback;

public class placeShulkerBlock {

    public static void register() {
        BlockPlacedCallback.EVENT.register(context -> {
            xyz.faewulf.diversity.event.placeShulkerBlock.run(context.getLevel(), context.getPlayer(), context.getItemInHand(), context.getClickedPos());
        });
    }
}
