package faewulf.diversity.mixin.shulkerBoxLabel;

import net.minecraft.entity.decoration.Brightness;
import net.minecraft.entity.decoration.DisplayEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(DisplayEntity.class)
public interface DisplayEntityMixin {
    @Invoker("setBillboardMode")
    abstract void invokeSetBillboardMode(DisplayEntity.BillboardMode billboardMode);

    @Invoker("setBrightness")
    abstract void invokeSetBrightness(Brightness brightness);
}