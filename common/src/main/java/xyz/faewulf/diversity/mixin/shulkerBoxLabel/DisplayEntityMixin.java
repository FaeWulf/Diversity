package xyz.faewulf.diversity.mixin.shulkerBoxLabel;

import net.minecraft.util.Brightness;
import net.minecraft.world.entity.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Display.class)
public interface DisplayEntityMixin {
    @Invoker("setBillboardConstraints")
    void invokeSetBillboardMode(Display.BillboardConstraints billboardMode);

    @Invoker("setBrightnessOverride")
    void invokeSetBrightness(Brightness brightness);
}