package xyz.faewulf.diversity.mixin.item.buildingBundle;

import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Mixin(Items.class)
public class ModifyComponentMixin {

    @Inject(method = "registerItem(Lnet/minecraft/resources/ResourceKey;Ljava/util/function/Function;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;", at = @At(value = "HEAD"))
    private static void propertiesInject(ResourceKey<Item> itemResourceKey, Function<Item.Properties, Item> propertiesItemFunction, Item.Properties properties, CallbackInfoReturnable<Item> cir) {

        List<String> diversity_Multiloader$whitelist = new ArrayList<>() {{
            add("minecraft:bundle");
            add("minecraft:white_bundle");
            add("minecraft:orange_bundle");
            add("minecraft:magenta_bundle");
            add("minecraft:light_blue_bundle");
            add("minecraft:yellow_bundle");
            add("minecraft:lime_bundle");
            add("minecraft:pink_bundle");
            add("minecraft:gray_bundle");
            add("minecraft:light_gray_bundle");
            add("minecraft:cyan_bundle");
            add("minecraft:purple_bundle");
            add("minecraft:blue_bundle");
            add("minecraft:brown_bundle");
            add("minecraft:green_bundle");
            add("minecraft:red_bundle");
            add("minecraft:black_bundle");
        }};

        if (diversity_Multiloader$whitelist.contains(itemResourceKey.location().toString())) {
            properties.component(DataComponents.ENCHANTABLE, new Enchantable(1));
        }
    }
}


