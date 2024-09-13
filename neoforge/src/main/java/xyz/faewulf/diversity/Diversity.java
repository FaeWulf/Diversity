package xyz.faewulf.diversity;


import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import xyz.faewulf.diversity.util.config.infoScreen.ModInfoScreen;

@Mod(Constants.MOD_ID)
public class Diversity {

    public Diversity(IEventBus eventBus) {
        Constants.LOG.info("Loading");

        //MidnightConfig.init(Constants.MOD_ID, ModConfigs.class);

        loadCommand();

        CommonClass.init();

        //config
        ModLoadingContext.get().registerExtensionPoint(
                IConfigScreenFactory.class,
                () -> (client, parent) -> ModInfoScreen.getScreen(parent)
        );

        Constants.LOG.info("Init done");
    }

    private void loadCommand() {
        Constants.LOG.info("Register commands...");
    }
}