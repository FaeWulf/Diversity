package xyz.faewulf.diversity.mixin.bonemealCoral;

import com.moulberry.mixinconstraints.annotations.IfModAbsent;
import net.minecraft.world.level.block.CoralFanBlock;
import org.spongepowered.asm.mixin.Mixin;
import xyz.faewulf.diversity.inter.ICustomFertilizeCoral;

@IfModAbsent(value = "carpet")
@Mixin(CoralFanBlock.class)
public abstract class CoralFanBlockMixin implements ICustomFertilizeCoral {
}
