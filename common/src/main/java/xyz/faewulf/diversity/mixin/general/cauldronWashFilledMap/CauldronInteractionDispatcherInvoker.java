package xyz.faewulf.diversity.mixin.general.cauldronWashFilledMap;

import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(CauldronInteraction.Dispatcher.class)
public interface CauldronInteractionDispatcherInvoker {
    @Invoker("put")
    void diversity$getPut(Item item, CauldronInteraction interaction);
}
