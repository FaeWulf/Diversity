package xyz.faewulf.diversity.mixin.RegistryLoadBlocker;

import net.minecraft.resources.RegistryDataLoader;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = RegistryDataLoader.class, priority = 1)
public abstract class RegistryLoaderMixin {

    /*
    @Inject(method = "loadElementFromResource", at = @At("HEAD"), cancellable = true)
    private static <E> void parseAndAddInject(WritableRegistry<E> registry, Decoder<E> decoder, RegistryOps<JsonElement> ops, ResourceKey<E> key, Resource resource, RegistrationInfo entryInfo, CallbackInfo ci) {
        if (key.registry().toString().equals("minecraft:enchantment")) {
            if (!ModConfigs.more_enchantment)
                if (CustomEnchantment.small_protection.contains(key.location().toString()))
                    ci.cancel();

            if (!ModConfigs.bundle_enchantment)
                if (CustomEnchantment.bundle_enchantments.contains(key.location().toString()))
                    ci.cancel();

        }
    }
     */


}

