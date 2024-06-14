package faewulf.diversity.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.item.ItemPlacementContext;

public interface BlockPlacedCallback {

    Event<BlockPlacedCallback> EVENT = EventFactory.createArrayBacked(BlockPlacedCallback.class,
            (listeners) -> (context -> {
                for (BlockPlacedCallback listener : listeners) {
                    listener.onBlockPlaced(context);
                }
            }));

    void onBlockPlaced(ItemPlacementContext context);
}
