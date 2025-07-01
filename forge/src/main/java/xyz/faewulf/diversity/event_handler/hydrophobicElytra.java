package xyz.faewulf.diversity.event_handler;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import xyz.faewulf.diversity.Constants;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class hydrophobicElytra {
    @SubscribeEvent
    public static void onEntityElytra(TickEvent.PlayerTickEvent.Post event) {
        if (event.player.isFallFlying())
            xyz.faewulf.diversity.event.hydrophobicElytra.run(event.player);
    }
}
