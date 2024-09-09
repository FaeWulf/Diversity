package xyz.faewulf.diversity.mixin.noLevelLimitAnvil;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.inventory.AnvilMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import xyz.faewulf.diversity.util.config.ModConfigs;

@Mixin(AnvilMenu.class)
public class AnvilBlockMixin {

    @ModifyExpressionValue(method = "createResult", at = @At(value = "CONSTANT", args = "intValue=40", ordinal = 2))
    private int ModifyLevelLimit(int original) {

        if (!ModConfigs.no_level_limit_anvil)
            return original;

        //maybe this is enough?
        return Integer.MAX_VALUE;
    }

}
