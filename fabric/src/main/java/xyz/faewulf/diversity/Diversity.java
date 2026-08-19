package xyz.faewulf.diversity;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditionType;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.pack.PackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import xyz.faewulf.diversity.command.emote;
import xyz.faewulf.diversity.event.onPreInitGame;
import xyz.faewulf.diversity.event_handler.*;
import xyz.faewulf.diversity.util.config.ModConfigs;

public class Diversity implements ModInitializer {

    @Override
    public void onInitialize() {
        Constants.LOG.info("Loading");


        loadCommand();
        loadEvent();

        CommonClass.init();

        Constants.LOG.info("Init done");

    }

    private void loadCommand() {
        Constants.LOG.info("Register commands...");
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            emote.register(dispatcher);
            //slimechunk.register(dispatcher);
        });
    }

    private void loadEvent() {
        Constants.LOG.info("Register events...");
        onPreInitGame.run();
        datapackLoaderEvent.run();
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
        onRightClickCropBlocks.register();
        shearDefusesTnt.register();
    }

}
