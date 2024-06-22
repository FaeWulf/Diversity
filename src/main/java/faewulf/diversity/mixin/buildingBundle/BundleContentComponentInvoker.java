package faewulf.diversity.mixin.buildingBundle;

import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.math.Fraction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BundleContentsComponent.class)
public interface BundleContentComponentInvoker {
    @Invoker("getOccupancy")
    public static Fraction getOccupancy(ItemStack stack) {
        throw new AssertionError();
    }
}
