package xyz.faewulf.diversity.event_handler;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import xyz.faewulf.diversity.Constants;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class hydrophobicElytra {
    @SubscribeEvent
    public static void onEntityElytra(PlayerTickEvent.Post event) {
        if (event.getEntity().isFallFlying())
            xyz.faewulf.diversity.event.hydrophobicElytra.run(event.getEntity());
    }
}
