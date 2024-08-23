package xyz.faewulf.diversity.mixin.buildingBundle;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockItem.class)
public interface BlockItemInvoker {
    @Invoker("getPlaceSound")
    abstract SoundEvent invokeGetPlaceSound(BlockState state);
}
