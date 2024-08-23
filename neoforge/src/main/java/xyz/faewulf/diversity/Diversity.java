package xyz.faewulf.diversity;


import eu.midnightdust.lib.config.MidnightConfig;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import xyz.faewulf.diversity.util.ModConfigs;

@Mod(Constants.MOD_ID)
public class Diversity {

    public Diversity(IEventBus eventBus) {
        Constants.LOG.info("Loading");

        MidnightConfig.init(Constants.MOD_ID, ModConfigs.class);

        loadCommand();

        CommonClass.init();

        Constants.LOG.info("Init done");
    }

    private void loadCommand() {
        Constants.LOG.info("Register commands...");
    }
}