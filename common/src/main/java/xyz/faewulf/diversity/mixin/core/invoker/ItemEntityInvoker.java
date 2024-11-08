package xyz.faewulf.diversity.mixin.core.invoker;

import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ItemEntity.class)
public interface ItemEntityInvoker {

    @Accessor
    void setAge(int value);
}
