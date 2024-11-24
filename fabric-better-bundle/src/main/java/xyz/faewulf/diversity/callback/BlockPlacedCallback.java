package xyz.faewulf.diversity.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.item.context.BlockPlaceContext;

public interface BlockPlacedCallback {

    Event<BlockPlacedCallback> EVENT = EventFactory.createArrayBacked(BlockPlacedCallback.class,
            (listeners) -> (context -> {
                for (BlockPlacedCallback listener : listeners) {
                    listener.onBlockPlaced(context);
                }
            }));

    void onBlockPlaced(BlockPlaceContext context);
}
