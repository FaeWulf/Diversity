package xyz.faewulf.diversity.event_handler;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import xyz.faewulf.diversity.Constants;
import xyz.faewulf.diversity.command.emote;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class onRegisterCommand {
    @SubscribeEvent
    // Register your command during the server starting event
    public static void onServerStarting(RegisterCommandsEvent event) {
        emote.register(event.getDispatcher());
    }
}
