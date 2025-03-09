package xyz.faewulf.diversity_better_bundle;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import xyz.faewulf.lib.api.v1.config.ConfigHelper;
import xyz.faewulf.lib.util.config.infoScreen.ModInfoScreen;

@Mod(Constants.MOD_ID)
public class Diversity {

    public Diversity(IEventBus eventBus) {
        Constants.LOG.info("Loading");

        //MidnightConfig.init(Constants.MOD_ID, ModConfigs.class);

        CommonClass.init();

        //config
        ModLoadingContext.get().registerExtensionPoint(
                IConfigScreenFactory.class,
                () -> (client, parent) -> {
                    ModInfoScreen modInfoScreen = (ModInfoScreen) ConfigHelper.getConfigScreen(parent, Constants.MOD_ID);
                    modInfoScreen.setUrls(null, Constants.WEBSITE, null, Constants.SOURCE_CODE);
                    return modInfoScreen;
                }
        );

        Constants.LOG.info("Init done");
    }
}