package xyz.faewulf.diversity.mixin.buildingBundle;

import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BundleContents;
import org.apache.commons.lang3.math.Fraction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.faewulf.diversity.inter.ICustomBundleContentBuilder;

import java.util.List;

@Mixin(BundleContents.Mutable.class)
public abstract class BuilderMixin implements ICustomBundleContentBuilder {

    @Shadow
    @Final
    private List<ItemStack> items;

    @Shadow
    private Fraction weight;

    @Unique
    private int diversity_Multiloader$maxSize = 64;

    @Inject(method = "getMaxAmountToAdd", at = @At("RETURN"), cancellable = true)
    private void getMaxAllowedInject(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        //value = used value
        int itemValue = Mth.mulAndTruncate(BundleContentComponentInvoker.getOccupancy(stack), 64);

        int usedSpace = Mth.mulAndTruncate(this.weight, 64);
        int freeSpace = diversity_Multiloader$maxSize - usedSpace;

        //freeSpace / (of 1 item of StackItem, how many item can put) or itemValue
        //which mean the return value is how much of that ItemStack can be put into the freeSpace

        //example: freeSpace = 60
        //if cobblestone, stack size is 64, itemValue is 1
        //-> can put 60 / 1 = 60 item into freespace, stack size is 64 then can only put 60

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
    public int diversity_Multiloader$getMaxSize() {
        return diversity_Multiloader$maxSize;
    }

    @Override
    public void diversity_Multiloader$setMaxSize(int maxSize) {

        if (maxSize < 64)
            return;
        this.diversity_Multiloader$maxSize = maxSize;
    }
}

