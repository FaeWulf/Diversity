package xyz.faewulf.diversity.event_handler;

import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xyz.faewulf.diversity.Constants;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public class blockDayCounterMessage {
    @SubscribeEvent
    public static boolean onChatReceived(ClientChatReceivedEvent event) {

        System.out.println("lmaooooo");

        if (!xyz.faewulf.diversity.event.blockDayCounterMessage.run(event.getMessage())) {
            return true;
        }

        return false;
    }
}
