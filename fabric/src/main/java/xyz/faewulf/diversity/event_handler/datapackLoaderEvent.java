package xyz.faewulf.diversity.event_handler;

import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.pack.PackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import xyz.faewulf.diversity.Constants;
import xyz.faewulf.diversity.util.config.ModConfigs;

public class datapackLoaderEvent {

    public static void run() {

        if (ModConfigs.more_enchantment) {
            FabricLoader.getInstance().getModContainer(Constants.MOD_ID).ifPresent(modContainer -> {
                ResourceLoader.registerBuiltinPack(
                        Identifier.fromNamespaceAndPath(Constants.MOD_ID, "backup_enchantments"), // Maps to resources/resourcepacks/<>
                        modContainer,
                        Component.literal("Diversity: Backup Enchantments Datapack"), // Display name
                        PackActivationType.ALWAYS_ENABLED // Forces it on unless toggled off by user
                );
            });
        }

        if (ModConfigs.bundle_enchantment) {
            FabricLoader.getInstance().getModContainer(Constants.MOD_ID).ifPresent(modContainer -> {
                ResourceLoader.registerBuiltinPack(
                        Identifier.fromNamespaceAndPath(Constants.MOD_ID, "bundle_enchantments"), // Maps to resources/resourcepacks/<>
                        modContainer,
                        Component.literal("Diversity: Bundle Enchantments Datapack"), // Display name
                        PackActivationType.ALWAYS_ENABLED // Forces it on unless toggled off by user
                );
            });
        }

        if (ModConfigs.sus_sand_recipe) {
            FabricLoader.getInstance().getModContainer(Constants.MOD_ID).ifPresent(modContainer -> {
                ResourceLoader.registerBuiltinPack(
                        Identifier.fromNamespaceAndPath(Constants.MOD_ID, "sus_sand_recipes"), // Maps to resources/resourcepacks/<>
                        modContainer,
                        Component.literal("Diversity: Sus sand recipes Datapack"), // Display name
                        PackActivationType.ALWAYS_ENABLED // Forces it on unless toggled off by user
                );
            });
        }
    }
}
