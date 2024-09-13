package xyz.faewulf.diversity.mixinClient;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import xyz.faewulf.diversity.util.config.ModConfigs;

@Mixin(value = AnvilScreen.class)
public abstract class AnvilMixin {
    @ModifyExpressionValue(method = "renderLabels", at = @At(value = "CONSTANT", args = "intValue=40", ordinal = 0))
    private int modifyLevelLimit(int original) {
        if (ModConfigs.no_level_limit_anvil)
            return Integer.MAX_VALUE;
        else
            return original;
    }
}
