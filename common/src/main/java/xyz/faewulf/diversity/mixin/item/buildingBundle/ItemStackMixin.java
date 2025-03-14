package xyz.faewulf.diversity.mixin.item.buildingBundle;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.faewulf.diversity.util.config.ModConfigs;

import java.util.List;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements DataComponentHolder {

    @Shadow
    public abstract Item getItem();

    @Inject(method = "getTooltipLines", at = @At(value = "RETURN"), cancellable = true)
    private void addExtraTooltip(Item.TooltipContext tooltipContext, Player player, TooltipFlag tooltipFlag, CallbackInfoReturnable<List<Component>> cir) {
        if (this.getItem() instanceof BundleItem && ModConfigs.bundle_place_mode) {
            List<Component> tooltip = cir.getReturnValue();
            tooltip.add(Component.translatable("item.diversity.bundle.change_mode.description").withStyle(ChatFormatting.DARK_GRAY));
            tooltip.add(Component.translatable("item.diversity.bundle.change_mode.description_2").withStyle(ChatFormatting.DARK_GRAY));
            cir.setReturnValue(tooltip);
        }
    }

}
