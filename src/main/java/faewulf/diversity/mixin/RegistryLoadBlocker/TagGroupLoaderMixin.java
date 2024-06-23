package faewulf.diversity.mixin.RegistryLoadBlocker;

import faewulf.diversity.util.ModConfigs;
import faewulf.diversity.util.data.CustomEnchantment;
import net.minecraft.registry.tag.TagGroupLoader;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Mixin(value = TagGroupLoader.class, priority = 1)
public class TagGroupLoaderMixin {

    @Inject(method = "buildGroup", at = @At(value = "INVOKE", target = "Ljava/util/Map;forEach(Ljava/util/function/BiConsumer;)V", shift = At.Shift.BEFORE))
    private <T> void buildGroupMixin(Map<Identifier, List<TagGroupLoader.TrackedEntry>> tags, CallbackInfoReturnable<Map<Identifier, Collection<T>>> cir) {
        List<TagGroupLoader.TrackedEntry> entryList = tags.get(Identifier.of("minecraft", "non_treasure"));
        if (entryList != null) {
            List<TagGroupLoader.TrackedEntry> tempList = entryList.stream().toList();
            for (TagGroupLoader.TrackedEntry trackedEntry : tempList) {
                String name = trackedEntry.entry().toString();

                if (!ModConfigs.more_enchantment)
                    if (CustomEnchantment.small_protection.contains(name))
                        entryList.remove(trackedEntry);

                if (!ModConfigs.bundle_enchantment)
                    if (CustomEnchantment.bundle_enchantments.contains(name))
                        entryList.remove(trackedEntry);
            }
        }
    }
}
