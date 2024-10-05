package xyz.faewulf.diversity;


import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import xyz.faewulf.diversity.command.emote;
import xyz.faewulf.diversity.util.config.infoScreen.ModInfoScreen;

@Mod(Constants.MOD_ID)
public class Diversity {

    public Diversity(IEventBus eventBus) {
        Constants.LOG.info("Loading");

        //MidnightConfig.init(Constants.MOD_ID, ModConfigs.class);

        loadCommand(eventBus);

        CommonClass.init();

        //config
        ModLoadingContext.get().registerExtensionPoint(
                IConfigScreenFactory.class,
                () -> (client, parent) -> ModInfoScreen.getScreen(parent)
        );

        Constants.LOG.info("Init done");
    }

    private void loadCommand(IEventBus eventBus) {
        // Subscribe to the event bus for server starting events
        Constants.LOG.info("Register commands...");
        eventBus.addListener(this::onServerStarting);
    }

    // Register your command during the server starting event
    private void onServerStarting(RegisterCommandsEvent event) {
        emote.register(event.getDispatcher());
    }
}