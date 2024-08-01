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

    @WrapOperation(method = "place(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/util/ActionResult;", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;decrementUnlessCreative(ILnet/minecraft/entity/LivingEntity;)V"))
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

}
