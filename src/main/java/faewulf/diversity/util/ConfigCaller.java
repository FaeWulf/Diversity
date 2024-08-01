package faewulf.diversity.util;

import eu.midnightdust.lib.config.MidnightConfig;
import faewulf.diversity.Diversity;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

public class ConfigCaller implements PreLaunchEntrypoint {
    @Override
    public void onPreLaunch() {
        MidnightConfig.init(Diversity.MODID, ModConfigs.class);
    }
}


