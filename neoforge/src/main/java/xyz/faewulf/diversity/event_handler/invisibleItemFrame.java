package xyz.faewulf.diversity.event_handler;

import net.minecraft.world.InteractionResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import xyz.faewulf.diversity.Constants;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class invisibleItemFrame {
    @SubscribeEvent
    public static void onEntityUse(PlayerInteractEvent.EntityInteract event) {
        InteractionResult interactionResult = xyz.faewulf.diversity.event.invisibleItemFrame.run(event.getLevel(), event.getEntity(), event.getHand(), event.getTarget(), null);

        if (interactionResult.consumesAction())
            event.setCanceled(true);
    }
}
