package faewulf.diversity.mixin.buildingBundle;

import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.sound.SoundEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockItem.class)
public interface BlockItemInvoker {
    @Invoker("getPlaceSound")
    abstract SoundEvent invokeGetPlaceSound(BlockState state);
}
