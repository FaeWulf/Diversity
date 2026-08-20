package xyz.faewulf.diversity_better_bundle.mixinClient.item.BuilderBundle;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientBundleTooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.item.component.BundleContents;
import org.apache.commons.lang3.math.Fraction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import xyz.faewulf.diversity_better_bundle.inter.ICustomBundleContents;

@Mixin(ClientBundleTooltip.class)
public abstract class ClientBundleTooltipMixin implements ClientTooltipComponent {


    @Shadow
    @Final
    private BundleContents contents;

    @WrapOperation(method = "extractBundleWithItemsTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/tooltip/ClientBundleTooltip;extractProgressbar(IILnet/minecraft/client/gui/Font;Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lorg/apache/commons/lang3/math/Fraction;)V"))
    private void extractProgressbarModifyWeightValue(int x, int y, Font font, GuiGraphicsExtractor graphics, Fraction weight, Operation<Void> original) {

        int usedSpace = Mth.mulAndTruncate(weight, 64);
        int maxValue = ((ICustomBundleContents) (Object) this.contents).diversity$getMaxSize();

        Fraction newWeight = Fraction.getFraction(usedSpace * 1f / maxValue);

        original.call(x, y, font, graphics, newWeight);
    }
}
