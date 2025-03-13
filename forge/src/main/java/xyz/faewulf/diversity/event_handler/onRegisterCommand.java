package xyz.faewulf.diversity.event_handler;

import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xyz.faewulf.diversity.Constants;
import xyz.faewulf.diversity.command.emote;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public class onRegisterCommand {
    @SubscribeEvent
    // Register your command during the server starting event
    public static void onServerStarting(RegisterCommandsEvent event) {
        emote.register(event.getDispatcher());
    }
}
