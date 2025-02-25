package xyz.faewulf.diversity.mixin.general.bundleEnchantments;


import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import xyz.faewulf.diversity.inter.ICustomBundleVacuum;

@Mixin(BundleItem.class)
public abstract class VacuumBundleItemMixin extends Item implements ICustomBundleVacuum {
    public VacuumBundleItemMixin(Properties properties) {
        super(properties);
    }
}
