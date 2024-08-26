package xyz.faewulf.diversity.mixin.RegistryLoadBlocker;

import net.minecraft.tags.TagLoader;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = TagLoader.class, priority = 1)
public class TagGroupLoaderMixin {

    /*
    @Inject(method = "build(Ljava/util/Map;)Ljava/util/Map;", at = @At(value = "INVOKE", target = "Ljava/util/Map;forEach(Ljava/util/function/BiConsumer;)V", shift = At.Shift.BEFORE))
    private <T> void buildGroupMixin(Map<ResourceLocation, List<TagLoader.EntryWithSource>> tags, CallbackInfoReturnable<Map<ResourceLocation, Collection<T>>> cir) {
        List<TagLoader.EntryWithSource> entryList = tags.get(new ResourceLocation("minecraft", "non_treasure"));
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
    }
     */
}
