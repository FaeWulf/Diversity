package faewulf.diversity.mixin.buildingBundle;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import faewulf.diversity.util.ModConfigs;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BundleItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockItem.class)
public abstract class BlockItemMixin extends Item {
    public BlockItemMixin(Settings settings) {
        super(settings);
    }

    //? if >=1.21 {
    /*@WrapOperation(method = "place(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/util/ActionResult;", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;decrementUnlessCreative(ILnet/minecraft/entity/LivingEntity;)V"))
    private void decrementUnlessCreativeWrapOperation(ItemStack instance, int amount, LivingEntity entity, Operation<Void> original) {
        if (!ModConfigs.bundle_place_mode) {
            original.call(instance, amount, entity);
            return;
        }

        if (instance.getItem() instanceof BundleItem)
            original.call(instance, 0, entity);
        else
            original.call(instance, amount, entity);
    }
    *///?}

    //? if =1.20.1 {
    @WrapOperation(method = "place(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/util/ActionResult;", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;decrement(I)V"))
    private void decrementWrapOperation(ItemStack instance, int amount, Operation<Void> original) {
        if (!ModConfigs.bundle_place_mode) {
            original.call(instance, amount);
            return;
        }

        if (instance.getItem() instanceof BundleItem)
            original.call(instance, 0);
        else
            original.call(instance, amount);
    }
    //?}
}