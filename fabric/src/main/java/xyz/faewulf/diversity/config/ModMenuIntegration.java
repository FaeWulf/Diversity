package xyz.faewulf.diversity.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import xyz.faewulf.diversity.util.config.Config;
import xyz.faewulf.diversity.util.config.ConfigScreen.ConfigScreen;

@Environment(EnvType.CLIENT)
public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        //return Config::createScreen;
        return parent -> ConfigScreen.getScreen(parent);
    }
}
