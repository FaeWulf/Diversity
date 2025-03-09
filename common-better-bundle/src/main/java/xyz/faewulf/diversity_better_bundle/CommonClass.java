package xyz.faewulf.diversity_better_bundle;

import net.minecraft.SharedConstants;
import xyz.faewulf.diversity_better_bundle.platform.Services;
import xyz.faewulf.diversity_better_bundle.util.config.ModConfigs;
import xyz.faewulf.lib.api.v1.config.ConfigHelper;
import xyz.faewulf.lib.api.v1.dev.GameTestHelper;

public class CommonClass {
    public static void init() {

        //Constants.LOG.info("Hello from Common init on {}! we are currently in a {} environment!", Services.PLATFORM.getPlatformName(), Services.PLATFORM.getEnvironmentName());
        //Constants.LOG.info("The ID for diamonds is {}", BuiltInRegistries.ITEM.getKey(Items.DIAMOND));

//        if (Services.PLATFORM.isModLoaded("examplemod")) {
//            Constants.LOG.info("Hello to examplemod");
//        }

        ConfigHelper.register(Constants.MOD_ID, ModConfigs.class);

        //for debug/testing
        if (Services.PLATFORM.isDevelopmentEnvironment())
            SharedConstants.IS_RUNNING_IN_IDE = true;

        //load config, moved to util.mixinPlugin.ConditionalMixinPlugin method: onLoad()
    }
}