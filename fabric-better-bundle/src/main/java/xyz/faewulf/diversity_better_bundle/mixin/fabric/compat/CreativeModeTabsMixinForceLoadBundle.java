package xyz.faewulf.diversity_better_bundle.mixin.fabric.compat;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.faewulf.diversity_better_bundle.util.config.ModConfigs;

@Pseudo
@Mixin(CreativeModeTabs.class)
public class CreativeModeTabsMixinForceLoadBundle {
    @Inject(method = "method_51328", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/CreativeModeTab$ItemDisplayParameters;enabledFeatures()Lnet/minecraft/world/flag/FeatureFlagSet;"))
    private static void injectItemBundle(CreativeModeTab.ItemDisplayParameters itemDisplayParameters, CreativeModeTab.Output output, CallbackInfo ci) {
        if (ModConfigs.bundle_recipe)
            output.accept(Items.BUNDLE);
    }
}
