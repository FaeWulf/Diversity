package xyz.faewulf.diversity_better_bundle.event_handler;

import net.minecraft.world.InteractionResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import xyz.faewulf.diversity_better_bundle.Constants;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class changeBundleMode {
    @SubscribeEvent
    public static void onPlayerLeftClockOnBlock(PlayerInteractEvent.LeftClickBlock event) {
        InteractionResult interactionResult = xyz.faewulf.diversity_better_bundle.event.changeBundleMode.run(event.getLevel(), event.getEntity(), event.getHand(), event.getPos(), event.getFace());

        if (interactionResult.consumesAction())
            event.setCanceled(true);
    }
}
