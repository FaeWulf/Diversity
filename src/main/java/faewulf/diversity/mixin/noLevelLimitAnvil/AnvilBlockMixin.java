package faewulf.diversity.mixin.noLevelLimitAnvil;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import faewulf.diversity.util.ModConfigs;
import net.minecraft.screen.AnvilScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AnvilScreenHandler.class)
public class AnvilBlockMixin {

    @ModifyExpressionValue(method = "updateResult", at = @At(value = "CONSTANT", args = "intValue=40", ordinal = 2))
    private int ModifyLevelLimit(int original) {

        if (!ModConfigs.no_level_limit_anvil)
            return original;

        //maybe this is enough?
        return Integer.MAX_VALUE;
    }

}
