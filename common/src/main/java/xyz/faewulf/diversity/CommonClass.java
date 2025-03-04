package xyz.faewulf.diversity;

import net.minecraft.SharedConstants;
import xyz.faewulf.diversity.platform.Services;
import xyz.faewulf.diversity.registry.CauldronInteractionRegister;
import xyz.faewulf.diversity.util.CustomLootTables;
import xyz.faewulf.diversity.util.config.ModConfigs;
import xyz.faewulf.lib.api.v1.config.ConfigHelper;
import xyz.faewulf.lib.api.v1.dev.GameTestHelper;

public class CommonClass {
    public static void init() {

        //Constants.LOG.info("Hello from Common init on {}! we are currently in a {} environment!", Services.PLATFORM.getPlatformName(), Services.PLATFORM.getEnvironmentName());
        //Constants.LOG.info("The ID for diamonds is {}", BuiltInRegistries.ITEM.getKey(Items.DIAMOND));

//        if (Services.PLATFORM.isModLoaded("examplemod")) {
//            Constants.LOG.info("Hello to examplemod");
//        }

        ConfigHelper.register(xyz.faewulf.diversity.Constants.MOD_ID, ModConfigs.class);
        GameTestHelper.register("xyz.faewulf.diversity.util.gameTests.entry");

        CustomLootTables.init();

        //registry
        CauldronInteractionRegister.register();

        //for debug/testing
        if (Services.PLATFORM.isDevelopmentEnvironment())
            SharedConstants.IS_RUNNING_IN_IDE = true;

        //load config, moved to util.mixinPlugin.ConditionalMixinPlugin method: onLoad()
        //Config.init();
    }
}