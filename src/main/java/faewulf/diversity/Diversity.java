package faewulf.diversity;

import com.mojang.brigadier.CommandDispatcher;
import eu.midnightdust.lib.config.MidnightConfig;
import faewulf.diversity.util.ModConfigs;
import faewulf.diversity.util.ReflectionUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Diversity implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("diversity");
    public static final String MODID = "diversity";

    @Override
    public void onInitialize() {
        LOGGER.info("Loading...");

        //load config
        MidnightConfig.init(Diversity.MODID, ModConfigs.class);

        loadCommand();
        loadEvent();

        LOGGER.info("Done!");
    }

    private void loadCommand() {
        LOGGER.info("Register commands...");
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            ReflectionUtils.invokeMethodOnClasses("faewulf.diversity.command", "register", CommandDispatcher.class, dispatcher);
        });
    }

    private void loadEvent() {
        LOGGER.info("Register events...");
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            ReflectionUtils.invokeClasses("faewulf.diversity.event", "run");
        });
    }
}