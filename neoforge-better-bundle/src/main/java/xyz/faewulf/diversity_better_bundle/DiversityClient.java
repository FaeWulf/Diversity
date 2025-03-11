package xyz.faewulf.diversity_better_bundle;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import xyz.faewulf.lib.api.v1.config.ConfigScreenHelper;
import xyz.faewulf.lib.util.config.infoScreen.ModInfoScreen;

@Mod(value = Constants.MOD_ID, dist = Dist.CLIENT)
public class DiversityClient {
    public DiversityClient(IEventBus modBus) {

        //config
        ModLoadingContext.get().registerExtensionPoint(
                IConfigScreenFactory.class,
                () -> (client, parent) -> {
                    ModInfoScreen modInfoScreen = (ModInfoScreen) ConfigScreenHelper.getConfigScreen(parent, Constants.MOD_ID);
                    modInfoScreen.setUrls(null, Constants.WEBSITE, null, Constants.SOURCE_CODE);
                    return modInfoScreen;
                }
        );
    }
}



