package xyz.faewulf.diversity_better_bundle.mixin.item.buildingBundle;

import net.minecraft.world.item.component.BundleContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import xyz.faewulf.diversity_better_bundle.inter.ICustomBundleContents;

@Mixin(BundleContents.class)
public abstract class BundleContentsMixin implements ICustomBundleContents {

    @Unique
    private int Diversity$maxSize = 0;

    @Override
    public void diversity$setMaxSize(int size) {
        this.Diversity$maxSize = size;
    }

    @Override
    public int diversity$getMaxSize() {
        return this.Diversity$maxSize;
    }
}
