package faewulf.diversity.compat;

import com.google.gson.JsonElement;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Map;

//compat with EasyShulkerBoxes mod, this inject will prevent bundle's interaction from this mod to override vanilla's bundle handling.
//Because Diversity is mixin into vanilla's code
@Pseudo
@Mixin(targets = "fuzs.iteminteractions.impl.world.item.container.ItemContentsProviders")
public class EasyShulkerBoxes_Compat {

    @Inject(method = "apply(Ljava/util/Map;Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/profiler/Profiler;)V", at = @At(value = "HEAD"))
    private void lmao(Map<Identifier, JsonElement> map, ResourceManager resourceManager, Profiler profiler, CallbackInfo ci) {
        ArrayList<Identifier> resourceLocation = new ArrayList<>();
        for (Identifier resourceLocation1 : map.keySet()) {
            if (resourceLocation1.toString().equals("easyshulkerboxes:bundle") || resourceLocation1.toString().equals("iteminteractions:bundle")) {
                resourceLocation.add(resourceLocation1);
            }
        }
        resourceLocation.forEach(map::remove);
    }
}