package xyz.faewulf.diversity;

import xyz.faewulf.diversity.registry.CauldronInteractionRegister;
import xyz.faewulf.diversity.util.CustomLootTables;
import xyz.faewulf.diversity.util.config.Config;

public class CommonClass {
    public static void init() {

        //Constants.LOG.info("Hello from Common init on {}! we are currently in a {} environment!", Services.PLATFORM.getPlatformName(), Services.PLATFORM.getEnvironmentName());
        //Constants.LOG.info("The ID for diamonds is {}", BuiltInRegistries.ITEM.getKey(Items.DIAMOND));

//        if (Services.PLATFORM.isModLoaded("examplemod")) {
//            Constants.LOG.info("Hello to examplemod");
//        }

        CustomLootTables.init();

        //registry
        CauldronInteractionRegister.register();
    }
}