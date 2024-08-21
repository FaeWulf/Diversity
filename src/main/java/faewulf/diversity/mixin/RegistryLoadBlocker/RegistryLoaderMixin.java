package faewulf.diversity.mixin.RegistryLoadBlocker;

//? if >=1.21 {

/*import com.google.gson.JsonElement;
import com.mojang.serialization.Decoder;
import faewulf.diversity.util.ModConfigs;
import faewulf.diversity.util.data.CustomEnchantment;
import net.minecraft.registry.MutableRegistry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryLoader;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.entry.RegistryEntryInfo;
import net.minecraft.resource.Resource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = RegistryLoader.class, priority = 1)
public abstract class RegistryLoaderMixin {

    @Inject(method = "parseAndAdd", at = @At("HEAD"), cancellable = true)
    private static <E> void parseAndAddInject(MutableRegistry<E> registry, Decoder<E> decoder, RegistryOps<JsonElement> ops, RegistryKey<E> key, Resource resource, RegistryEntryInfo entryInfo, CallbackInfo ci) {
        if (key.getRegistry().toString().equals("minecraft:enchantment")) {
            if (!ModConfigs.more_enchantment)
                if (CustomEnchantment.small_protection.contains(key.getValue().toString()))
                    ci.cancel();

            if (!ModConfigs.bundle_enchantment)
                if (CustomEnchantment.bundle_enchantments.contains(key.getValue().toString()))
                    ci.cancel();

        }
    }
}
 
*///?}
