package faewulf.diversity;

import com.mojang.brigadier.CommandDispatcher;
import faewulf.diversity.util.ModConfigs;
import faewulf.diversity.util.ReflectionUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Diversity implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("diversity");
    public static final String MODID = "diversity";
    public static ModConfigs configs;

    public static final Identifier modIdentifier = Identifier.of(MODID);

    @Override
    public void onInitialize() {
        LOGGER.info("Loading...");

        //load config
        //MidnightConfig.init(Diversity.MODID, ModConfigs.class);

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

    private static final TagKey<Enchantment> NON_TREASURE_TAG = TagKey.of(RegistryKeys.ENCHANTMENT, Identifier.of("minecraft:non_treasure"));

    private void loadEvent() {
        LOGGER.info("Register events...");
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            ReflectionUtils.invokeClasses("faewulf.diversity.event", "run");
        });
    }
}