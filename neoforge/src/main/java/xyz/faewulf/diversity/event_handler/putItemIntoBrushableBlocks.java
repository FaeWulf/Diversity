package xyz.faewulf.diversity.event_handler;

import net.minecraft.world.InteractionResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import xyz.faewulf.diversity.Constants;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class putItemIntoBrushableBlocks {
    @SubscribeEvent
    public static void onUseItemOnBlock(PlayerInteractEvent.RightClickBlock event) {
        InteractionResult interactionResult = xyz.faewulf.diversity.event.putItemIntoBrushableBlocks.run(event.getLevel(), event.getEntity(), event.getHand(), event.getHitVec());

        if (interactionResult.consumesAction())
            event.setCanceled(true);
    }
}