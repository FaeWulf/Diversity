package xyz.faewulf.diversity.mixin.RegistryLoadBlocker;

import com.google.gson.JsonElement;
import com.mojang.serialization.Decoder;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.WritableRegistry;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.resources.Resource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.faewulf.diversity.util.ModConfigs;
import xyz.faewulf.diversity.util.data.CustomEnchantment;

@Mixin(value = RegistryDataLoader.class, priority = 1)
public abstract class RegistryLoaderMixin {

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


}

