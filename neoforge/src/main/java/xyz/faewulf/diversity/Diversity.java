package xyz.faewulf.diversity;


import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class Diversity {

    public Diversity(IEventBus eventBus) {
        Constants.LOG.info("Loading");

        //MidnightConfig.init(Constants.MOD_ID, ModConfigs.class);

        CommonClass.init();

        Constants.LOG.info("Init done");
    }
}