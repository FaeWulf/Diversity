package xyz.faewulf.diversity_better_bundle.event_handler;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import xyz.faewulf.diversity_better_bundle.Constants;
import xyz.faewulf.diversity_better_bundle.util.config.ModConfigs;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class datapackLoaderEvent {

    @SubscribeEvent
    public static void onAddPackFinders(AddPackFindersEvent event) {
        // Validate if the data pack is requested and the event target is SERVER_DATA
        if (event.getPackType() == PackType.SERVER_DATA) {
            if (ModConfigs.bundle_enchantment) {
                event.addPackFinders(
                        Identifier.fromNamespaceAndPath(Constants.MOD_ID, "resourcepacks/bundle_enchantments"), // Maps to resources/resourcepacks/<>
                        PackType.SERVER_DATA,
                        Component.literal("Diversity: Bundle Enchantments Datapack"), // Display name
                        PackSource.BUILT_IN,
                        true, // Forces it on unless toggled off by user
                        Pack.Position.TOP
                );
            }
        }
    }
}
