package faewulf.diversity.mixin.buildingBundle;

import faewulf.diversity.util.ModConfigs;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BundleItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BlockItem.class)
public abstract class BlockItemMixin extends Item {
    public BlockItemMixin(Settings settings) {
        super(settings);
    }

    @Redirect(method = "place(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/util/ActionResult;", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;decrementUnlessCreative(ILnet/minecraft/entity/LivingEntity;)V"))
    public void decrementUnlessCreativeRedirect(ItemStack instance, int amount, LivingEntity entity) {

        if (!ModConfigs.bundle_place_mode)
            return;

        if (instance.getItem() instanceof BundleItem)
            instance.decrement(0);
        else
            instance.decrementUnlessCreative(amount, entity);
    }

}
