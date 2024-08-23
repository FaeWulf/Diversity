package xyz.faewulf.diversity.mixin.bonemealCoral;

import com.moulberry.mixinconstraints.annotations.IfModAbsent;
import net.minecraft.world.level.block.CoralPlantBlock;
import org.spongepowered.asm.mixin.Mixin;
import xyz.faewulf.diversity.inter.ICustomFertilizeCoral;

@IfModAbsent(value = "carpet")
@Mixin(CoralPlantBlock.class)
public abstract class CoralBlockMixin implements ICustomFertilizeCoral {
}
