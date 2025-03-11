package xyz.faewulf.diversity_better_bundle;


import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import xyz.faewulf.diversity_better_bundle.event_handler.RegsitryDone;
import xyz.faewulf.diversity_better_bundle.platform.RegisterEnchantment;
import xyz.faewulf.lib.api.v1.config.ConfigScreenHelper;
import xyz.faewulf.lib.util.config.infoScreen.ModInfoScreen;

@Mod(Constants.MOD_ID)
public class Diversity {

    public Diversity() {
        Constants.LOG.info("Loading");

        //register enchant
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.register(RegsitryDone.class);

        RegisterEnchantment.ENCHANTMENTS.register(modEventBus);
        RegisterEnchantment.init();

        CommonClass.init();

        Constants.LOG.info("Init done");
    }

    // Client-side
    @Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            //config
            ModLoadingContext.get().registerExtensionPoint(
                    ConfigScreenHandler.ConfigScreenFactory.class,
                    () -> new ConfigScreenHandler.ConfigScreenFactory(
                            (client, parent) -> {
                                ModInfoScreen modInfoScreen = (ModInfoScreen) ConfigScreenHelper.getConfigScreen(parent, Constants.MOD_ID);
                                modInfoScreen.setUrls(null, Constants.WEBSITE, null, Constants.SOURCE_CODE);
                                return modInfoScreen;
                            }
                    )
            );
        }
    }

}