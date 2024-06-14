package faewulf.diversity.event;

import faewulf.diversity.util.ConfigHandler;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

public class onReload {
    public static void run() {
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
            @Override
            public Identifier getFabricId() {
                return null;
            }

            @Override
            public void reload(ResourceManager manager) {
                ConfigHandler.loadConfig();
            }
        });
    }
}
