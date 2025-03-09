package xyz.faewulf.diversity_better_bundle.mixin.general.bundleEnchantments;


import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import xyz.faewulf.diversity_better_bundle.inter.ICustomBundleVacuum;

@Mixin(BundleItem.class)
public abstract class VacuumBundleItemMixin extends Item implements ICustomBundleVacuum {
    public VacuumBundleItemMixin(Properties properties) {
        super(properties);
    }
}
