package xyz.faewulf.diversity.mixin.buildingBundle;

import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Function;

@Mixin(Items.class)
public class ModifyComponentMixin {

    @Inject(method = "registerItem(Lnet/minecraft/resources/ResourceKey;Ljava/util/function/Function;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;", at = @At(value = "HEAD"))
    private static void propertiesInject(ResourceKey<Item> itemResourceKey, Function<Item.Properties, Item> propertiesItemFunction, Item.Properties properties, CallbackInfoReturnable<Item> cir) {
        if (itemResourceKey.location().toString().equals("minecraft:bundle")) {
            properties.component(DataComponents.ENCHANTABLE, new Enchantable(1));
        }
    }
}


