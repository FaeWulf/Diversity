package faewulf.diversity.mixin.usableSuspiciousBlock;

import faewulf.diversity.inter.ICustomBrushableBlockEntity;
import net.minecraft.block.entity.BrushableBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BrushableBlockEntity.class)
public class BrushableBlockEntityMixin implements ICustomBrushableBlockEntity {

    @Shadow
    private ItemStack item;

    @Override
    public void setItem(ItemStack item) {
        if (this.item == ItemStack.EMPTY || this.item.getItem() == Items.AIR)
            this.item = item;
    }
}
