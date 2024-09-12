package xyz.faewulf.diversity.mixin.buildingBundle;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import xyz.faewulf.diversity.util.config.ModConfigs;

@Mixin(BlockItem.class)
public abstract class BlockItemMixin extends Item {
    public BlockItemMixin(Properties settings) {
        super(settings);
    }

    @WrapOperation(method = "place", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;shrink(I)V"))
    private void decrementUnlessCreativeWrapOperation(ItemStack instance, int $$0, Operation<Void> original) {
        if (!ModConfigs.bundle_place_mode) {
            original.call(instance, $$0);
            return;
        }

        if (instance.getItem() instanceof BundleItem)
            original.call(instance, 0);
        else
            original.call(instance, $$0);
    }

}
