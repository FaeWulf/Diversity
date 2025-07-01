package xyz.faewulf.diversity_better_bundle;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import xyz.faewulf.lib.api.v1.config.ConfigScreenHelper;
import xyz.faewulf.lib.util.config.infoScreen.ModInfoScreen;

@Mod(Constants.MOD_ID)
public class Diversity {

    public Diversity() {
        Constants.LOG.info("Loading");

        CommonClass.init();

        Constants.LOG.info("Init done");
    }

    @Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            //config
            MinecraftForge.registerConfigScreen((client, parent) -> {
                ModInfoScreen modInfoScreen = (ModInfoScreen) ConfigScreenHelper.getConfigScreen(parent, Constants.MOD_ID);
                modInfoScreen.setUrls(null, Constants.WEBSITE, null, Constants.SOURCE_CODE);
                return modInfoScreen;
            });
        }
    }
}