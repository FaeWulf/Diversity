package xyz.faewulf.diversity.event_handler;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientChatReceivedEvent;
import xyz.faewulf.diversity.Constants;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class blockDayCounterMessage {
    @SubscribeEvent
    public static void onChatReceived(ClientChatReceivedEvent event) {
        if (!xyz.faewulf.diversity.event.blockDayCounterMessage.run(event.getMessage())) {
            event.setCanceled(true);
        }
    }
}
