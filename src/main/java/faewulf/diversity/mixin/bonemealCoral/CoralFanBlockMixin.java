package faewulf.diversity.mixin.bonemealCoral;

import faewulf.diversity.inter.ICustomFertilizeCoral;
import net.minecraft.block.CoralFanBlock;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CoralFanBlock.class)
public abstract class CoralFanBlockMixin implements ICustomFertilizeCoral {
}
