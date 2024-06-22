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
            int itemValue = MathHelper.multiplyFraction(BundleContentComponentInvoker.getOccupancy(stack), 64);

            int usedSpace = MathHelper.multiplyFraction(this.occupancy, 64);
            int freeSpace = maxSize - usedSpace;

            //freeSpace / (of 1 item of StackItem, how many item can put) or itemValue
            //which mean the return value is how much of that ItemStack can be put into the freeSpace

            //example: freeSpace = 60
            //if cobblestone, stack size is 64, itemValue is 1
            //-> can put 60 / 1 = 60 item into freespace, stack size is 64 then can only put 4

            //if pearl, stack size is 16, itemValue is 4
            //-> can put 60 / 4 = 15 item into freespace, stack size is 16 then can only put 15

            //if sword, stack size is 1, item value is 64
            //-> can put 60 / 64 = 0 (round down), so can't put in

            //if bundle, inside that bundle is 64 items, bundle value is 4
            //-> can put 60 / (4 + 64) = 0, so can't put in

            //System.out.println("real: " + Math.max(freeSpace / itemValue, 0));
            //System.out.println("expected: " + cir.getReturnValue());
            cir.setReturnValue(Math.max(freeSpace / itemValue, 0));
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
