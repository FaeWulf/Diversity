package faewulf.diversity.mixin.noLevelLimitAnvil;

import faewulf.diversity.util.ModConfigs;
import net.minecraft.screen.AnvilScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(AnvilScreenHandler.class)
public class AnvilBlockMixin {

    @ModifyConstant(method = "updateResult", constant = @Constant(intValue = 40, ordinal = 2))
    private int Modify40LevelLimit(int constant) {

        if (!ModConfigs.no_level_limit_anvil)
            return constant;

        //maybe this is enough?
        return Integer.MAX_VALUE;
    }

}
