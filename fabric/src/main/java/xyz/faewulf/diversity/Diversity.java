package xyz.faewulf.diversity;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import xyz.faewulf.diversity.command.emote;
import xyz.faewulf.diversity.command.slimechunk;
import xyz.faewulf.diversity.event_handler.*;
import xyz.faewulf.diversity.platform.RegisterEnchantment;
import xyz.faewulf.diversity.util.config.Config;

public class Diversity implements ModInitializer {

    @Override
    public void onInitialize() {
        Constants.LOG.info("Loading");

        Config.init();

        loadCommand();
        loadEvent();

        RegisterEnchantment.register();
        CommonClass.init();

        Constants.LOG.info("Init done");
    }

    private void loadCommand() {
        Constants.LOG.info("Register commands...");
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            emote.register(dispatcher);
            slimechunk.register(dispatcher);
        });
    }

    private void loadEvent() {
        Constants.LOG.info("Register events...");
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            breakCrops.register();
            changeBundleMode.register();
            explodeSniffer.register();
            hydrophobicElytra.register();
            invisibleItemFrame.register();
            invisibleItemFrame.register();
            placeShulkerBlock.register();
            placeWetSpongeBlock.register();
            playerChatEmote.register();
            putItemIntoBrushableBlocks.register();
            useClockOnBlock.register();
            useShearOnBlock.register();
        });
    }

}
