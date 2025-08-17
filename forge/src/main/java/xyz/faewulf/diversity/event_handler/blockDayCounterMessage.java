package xyz.faewulf.diversity.event_handler;

import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xyz.faewulf.diversity.Constants;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public class blockDayCounterMessage {
    @SubscribeEvent
    public static void onChatReceived(ClientChatReceivedEvent event) {
        if (!xyz.faewulf.diversity.event.blockDayCounterMessage.run(event.getMessage())) {
            event.setCanceled(true);
        }
    }
}
