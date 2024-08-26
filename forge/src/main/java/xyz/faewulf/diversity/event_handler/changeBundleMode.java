package xyz.faewulf.diversity.event_handler;

import net.minecraft.world.InteractionResult;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xyz.faewulf.diversity.Constants;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public class changeBundleMode {
    @SubscribeEvent
    public static void onPlayerLeftClockOnBlock(PlayerInteractEvent.LeftClickBlock event) {
        InteractionResult interactionResult = xyz.faewulf.diversity.event.changeBundleMode.run(event.getLevel(), event.getEntity(), event.getHand(), event.getPos(), event.getFace());

        if (interactionResult.consumesAction())
            event.setCanceled(true);
    }
}
