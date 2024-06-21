package faewulf.diversity.mixin.buildingBundle;

import faewulf.diversity.inter.ICustomBundleContentBuilder;
import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang3.math.Fraction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(BundleContentsComponent.class)
public abstract class BundleContentsComponentMixin implements TooltipData {

    @Mixin(BundleContentsComponent.Builder.class)
    public abstract static class BuilderMixin implements ICustomBundleContentBuilder {

        @Shadow
        @Final
        private List<ItemStack> stacks;

        @Shadow
        private Fraction occupancy;
        @Unique
        private int maxSize = 64;

        @Inject(method = "getMaxAllowed", at = @At("RETURN"), cancellable = true)
        private void getMaxAllowedInject(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
            //value = used value
            int value = MathHelper.multiplyFraction(this.occupancy, 64);
            cir.setReturnValue(maxSize - value);
        }

        @Override
        public int getMaxSize() {
            return maxSize;
        }

        @Override
        public void setMaxSize(int maxSize) {

            if (maxSize < 64)
                return;
            this.maxSize = maxSize;
        }
    }

}
