package faewulf.diversity.mixin.bonemealCoral;

import faewulf.diversity.inter.ICustomFertilizeCoral;
import net.minecraft.block.CoralBlock;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CoralBlock.class)
public abstract class CoralBlockMixin implements ICustomFertilizeCoral {
}
