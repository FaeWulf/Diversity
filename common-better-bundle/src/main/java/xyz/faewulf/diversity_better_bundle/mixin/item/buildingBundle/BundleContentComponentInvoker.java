package xyz.faewulf.diversity_better_bundle.mixin.item.buildingBundle;

import com.mojang.serialization.DataResult;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BundleContents;
import org.apache.commons.lang3.math.Fraction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BundleContents.class)
public interface BundleContentComponentInvoker {
    @Invoker("getWeight")
    static DataResult<Fraction> getOccupancy(ItemInstance item) {
        throw new AssertionError();
    }
}

