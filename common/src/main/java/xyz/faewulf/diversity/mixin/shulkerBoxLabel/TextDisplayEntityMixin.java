package xyz.faewulf.diversity.mixin.shulkerBoxLabel;

import net.minecraft.world.entity.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Display.TextDisplay.class)
public interface TextDisplayEntityMixin {
    @Invoker("setTextOpacity")
    void invokeSetTextOpacity(byte textOpacity);

    @Invoker("setBackgroundColor")
    void invokeSetBackground(int background);
}
