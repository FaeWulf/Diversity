package xyz.faewulf.diversity_better_bundle.event_handler;

import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.pack.PackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import xyz.faewulf.diversity_better_bundle.Constants;
import xyz.faewulf.diversity_better_bundle.util.config.ModConfigs;

public class datapackLoaderEvent {

    public static void run() {
        if (ModConfigs.bundle_enchantment) {
            FabricLoader.getInstance().getModContainer(Constants.MOD_ID).ifPresent(modContainer -> {
                ResourceLoader.registerBuiltinPack(
                        Identifier.fromNamespaceAndPath(Constants.MOD_ID, "bundle_enchantments"), // Maps to resources/resourcepacks/<>
                        modContainer,
                        Component.literal("Diversity Better Bundle: Bundle Enchantments Datapack"), // Display name
                        PackActivationType.ALWAYS_ENABLED // Forces it on unless toggled off by user
                );
            });
        }
    }
}
