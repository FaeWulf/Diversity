package xyz.faewulf.diversity.mixin.usableSuspiciousBlock;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import xyz.faewulf.diversity.inter.ICustomBrushableBlockEntity;

@Mixin(BrushableBlockEntity.class)
public class BrushableBlockEntityMixin implements ICustomBrushableBlockEntity {

    @Shadow
    private ItemStack item;

    @Override
    public void multiLoader_1_20_1$setItem(ItemStack item) {
        if (this.item == ItemStack.EMPTY || this.item.getItem() == Items.AIR)
            this.item = item;
    }
}
