package xyz.faewulf.diversity.event_handler;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import xyz.faewulf.diversity.Constants;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class hydrophobicElytra {
    @SubscribeEvent
    public static void onEntityElytra(TickEvent.PlayerTickEvent event) {
        if (event.player.isFallFlying() && event.phase == TickEvent.Phase.END)
            xyz.faewulf.diversity.event.hydrophobicElytra.run(event.player);
    }
}
