package xyz.faewulf.diversity.mixin.core.recipeLoadBlocker;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeAccess;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.faewulf.diversity.Constants;
import xyz.faewulf.diversity.util.config.ModConfigs;

import java.util.SortedMap;

@Mixin(RecipeManager.class)
public abstract class RecipeManagerMixin extends SimplePreparableReloadListener<RecipeMap> implements RecipeAccess {

    @Inject(
            method = "prepare(Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)Lnet/minecraft/world/item/crafting/RecipeMap;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/packs/resources/SimpleJsonResourceReloadListener;scanDirectory(Lnet/minecraft/server/packs/resources/ResourceManager;Ljava/lang/String;Lcom/mojang/serialization/DynamicOps;Lcom/mojang/serialization/Codec;Ljava/util/Map;)V", shift = At.Shift.AFTER))
    private void prepareInject(ResourceManager p_379845_, ProfilerFiller p_380058_, CallbackInfoReturnable<RecipeMap> cir, @Local SortedMap<ResourceLocation, Recipe<?>> sortedmap) {
        if (!ModConfigs.sus_sand_recipe) {
            sortedmap.remove(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "suspicious_gravel"));
            sortedmap.remove(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "suspicious_sand"));
        }
    }
}
