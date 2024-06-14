package faewulf.diversity;

import com.mojang.brigadier.CommandDispatcher;
import faewulf.diversity.util.ConfigHandler;
import faewulf.diversity.util.ModConfigs;
import faewulf.diversity.util.ReflectionUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Diversity implements ModInitializer {
    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("diversity");
    public static final String MODID = "diversity";
    public static ModConfigs configs;

    public static final Identifier modIdentifier = Identifier.of(MODID);

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        LOGGER.info("Loading...");

        //load config
        ConfigHandler.loadConfig();

        loadCommand();
        loadEvent();
    }

    private void loadCommand() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            ReflectionUtils.invokeMethodOnClasses("faewulf.diversity.command", "register", CommandDispatcher.class, dispatcher);
        });
    }

    private void loadEvent() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            ReflectionUtils.invokeClasses("faewulf.diversity.event", "run");
        });
    }
}