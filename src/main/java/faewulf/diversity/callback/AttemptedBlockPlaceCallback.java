package faewulf.diversity.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.ActionResult;

public interface AttemptedBlockPlaceCallback {

    Event<AttemptedBlockPlaceCallback> EVENT = EventFactory.createArrayBacked(AttemptedBlockPlaceCallback.class,
            (listeners) -> (context -> {
                for (AttemptedBlockPlaceCallback listener : listeners) {
                    ActionResult result = listener.onBlockAttemptedPlace(context);
                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }
                return ActionResult.PASS;
            }));

    ActionResult onBlockAttemptedPlace(ItemPlacementContext context);

}