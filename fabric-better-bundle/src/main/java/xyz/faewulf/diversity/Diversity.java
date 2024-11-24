package xyz.faewulf.diversity;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import xyz.faewulf.diversity.event_handler.*;
import xyz.faewulf.diversity.platform.RegisterEnchantment;

public class Diversity implements ModInitializer {

    @Override
    public void onInitialize() {
        Constants.LOG.info("Loading");

        loadEvent();

        RegisterEnchantment.register();
        CommonClass.init();

        Constants.LOG.info("Init done");
    }

    private void loadEvent() {
        Constants.LOG.info("Register events...");
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            changeBundleMode.register();
        });
    }

}
