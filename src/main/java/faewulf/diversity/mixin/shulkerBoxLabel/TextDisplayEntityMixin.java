package faewulf.diversity.mixin.shulkerBoxLabel;

import net.minecraft.entity.decoration.DisplayEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(DisplayEntity.TextDisplayEntity.class)
public interface TextDisplayEntityMixin {
    @Invoker("setTextOpacity")
    abstract void invokeSetTextOpacity(byte textOpacity);

    @Invoker("setBackground")
    abstract void invokeSetBackground(int background);
}
