package xyz.faewulf.diversity.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import xyz.faewulf.diversity.Constants;
import xyz.faewulf.lib.api.v1.config.ConfigHelper;
import xyz.faewulf.lib.util.config.infoScreen.ModInfoScreen;

@Environment(EnvType.CLIENT)
public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return screen -> {
            xyz.faewulf.lib.util.config.infoScreen.ModInfoScreen modInfoScreen = (ModInfoScreen) ConfigHelper.getConfigScreen(screen, Constants.MOD_ID);
            modInfoScreen.setUrls(null, Constants.WEBSITE, null, Constants.SOURCE_CODE);
            return modInfoScreen;
        };
    }
}
