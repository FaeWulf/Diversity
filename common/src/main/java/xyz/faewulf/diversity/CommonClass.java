package xyz.faewulf.diversity;

import xyz.faewulf.diversity.registry.CauldronInteractionRegister;

public class CommonClass {
    public static void init() {

        //Constants.LOG.info("Hello from Common init on {}! we are currently in a {} environment!", Services.PLATFORM.getPlatformName(), Services.PLATFORM.getEnvironmentName());
        //Constants.LOG.info("The ID for diamonds is {}", BuiltInRegistries.ITEM.getKey(Items.DIAMOND));

//        if (Services.PLATFORM.isModLoaded("examplemod")) {
//            Constants.LOG.info("Hello to examplemod");
//        }

        CauldronInteractionRegister.register();

        //load config, moved to util.mixinPlugin.ConditionalMixinPlugin method: onLoad()
        //Config.init();
    }
}