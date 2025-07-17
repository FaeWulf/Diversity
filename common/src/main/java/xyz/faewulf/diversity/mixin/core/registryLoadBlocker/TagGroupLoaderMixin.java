package xyz.faewulf.diversity.mixin.core.registryLoadBlocker;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.faewulf.diversity.Constants;
import xyz.faewulf.diversity.platform.Services;
import xyz.faewulf.diversity.registry.CustomEnchantment;
import xyz.faewulf.diversity.util.config.ModConfigs;

import java.util.Collection;
import java.util.List;
import java.util.Map;

// This mixin prevent game crash when you disable mod's custom enchantments
@Mixin(value = TagLoader.class, priority = 1)
public class TagGroupLoaderMixin {

    @Inject(method = "build(Ljava/util/Map;)Ljava/util/Map;", at = @At(value = "INVOKE", target = "Ljava/util/Map;forEach(Ljava/util/function/BiConsumer;)V"))
    private <T> void buildGroupMixin(Map<ResourceLocation, List<TagLoader.EntryWithSource>> tags, CallbackInfoReturnable<Map<ResourceLocation, Collection<T>>> cir) {
        // In minecraft:non_treasure enchantment tag
        List<TagLoader.EntryWithSource> entryList = tags.get(ResourceLocation.fromNamespaceAndPath("minecraft", "non_treasure"));
        if (entryList != null) {
            List<TagLoader.EntryWithSource> tempList = entryList.stream().toList();
            for (TagLoader.EntryWithSource trackedEntry : tempList) {
                String name = trackedEntry.entry().toString();

                if (!ModConfigs.more_enchantment)
                    if (CustomEnchantment.small_protection.contains(name))
                        entryList.remove(trackedEntry);

                if (!ModConfigs.bundle_enchantment)
                    if (CustomEnchantment.bundle_enchantments.contains(name))
                        entryList.remove(trackedEntry);
            }
        }

        // In minecraft:treasure enchantment tag
        List<TagLoader.EntryWithSource> entryList2 = tags.get(ResourceLocation.fromNamespaceAndPath("minecraft", "treasure"));
        if (entryList2 != null) {
            List<TagLoader.EntryWithSource> tempList = entryList2.stream().toList();
            for (TagLoader.EntryWithSource trackedEntry : tempList) {
                String name = trackedEntry.entry().toString();

                if (!ModConfigs.more_enchantment)
                    if (CustomEnchantment.small_protection.contains(name))
                        entryList2.remove(trackedEntry);

                if (!ModConfigs.bundle_enchantment)
                    if (CustomEnchantment.bundle_enchantments.contains(name))
                        entryList2.remove(trackedEntry);
            }
        }

        // In diversity:leatherworker enchantment tag
        List<TagLoader.EntryWithSource> entryList3 = tags.get(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "leatherworker"));
        if (entryList3 != null) {
            List<TagLoader.EntryWithSource> tempList = entryList3.stream().toList();
            for (TagLoader.EntryWithSource trackedEntry : tempList) {
                String name = trackedEntry.entry().toString();

                if (!ModConfigs.bundle_enchantment)
                    if (CustomEnchantment.bundle_enchantments.contains(name))
                        entryList3.remove(trackedEntry);
            }
        }

        // In diversity:bundles enchantment tag
        // Remove metal bundle item tag if metal bundle mod not loaded
        List<TagLoader.EntryWithSource> entryList4 = tags.get(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "bundles"));
        if (entryList4 != null) {
            List<TagLoader.EntryWithSource> tempList = entryList4.stream().toList();
            for (TagLoader.EntryWithSource trackedEntry : tempList) {
                String name = trackedEntry.entry().toString();

                if (!Services.PLATFORM.isModLoaded("metalbundles"))
                    if (name.contains("metalbundles"))
                        entryList4.remove(trackedEntry);
            }
        }
    }
}
