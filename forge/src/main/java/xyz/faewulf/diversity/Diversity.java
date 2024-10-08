package xyz.faewulf.diversity;


import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import xyz.faewulf.diversity.command.emote;
import xyz.faewulf.diversity.event_handler.RegsitryDone;
import xyz.faewulf.diversity.platform.RegisterEnchantment;
import xyz.faewulf.diversity.util.config.infoScreen.ModInfoScreen;

@Mod(Constants.MOD_ID)
public class Diversity {

    public Diversity() {
        Constants.LOG.info("Loading");

        loadCommand();

        //config
        ModLoadingContext.get().registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory(
                        (client, parent) -> ModInfoScreen.getScreen(parent)
                )
        );


        //register enchant
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.register(RegsitryDone.class);

        RegisterEnchantment.ENCHANTMENTS.register(modEventBus);
        RegisterEnchantment.init();

        CommonClass.init();

        Constants.LOG.info("Init done");
    }

    private void loadCommand() {
        Constants.LOG.info("Register commands...");
        MinecraftForge.EVENT_BUS.addListener(this::onServerStarting);
    }

    // Register your command during the server starting event
    private void onServerStarting(RegisterCommandsEvent event) {
        emote.register(event.getDispatcher());
    }
}