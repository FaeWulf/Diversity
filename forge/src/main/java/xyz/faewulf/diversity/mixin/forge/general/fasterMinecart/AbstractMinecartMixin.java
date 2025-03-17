package xyz.faewulf.diversity.mixin.forge.general.fasterMinecart;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.VehicleEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.extensions.IForgeAbstractMinecart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import xyz.faewulf.diversity.util.config.ModConfigs;
import xyz.faewulf.lib.util.Compare;

@Mixin(AbstractMinecart.class)
public abstract class AbstractMinecartMixin extends VehicleEntity implements IForgeAbstractMinecart {

    @Unique
    private BlockPos diversity$lastPos;

    @Unique
    private double diversity$lastMaxSpeedMult = 1;

    public AbstractMinecartMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @ModifyReturnValue(method = "getMaxSpeedWithRail", at = @At("RETURN"))
    private double getMaxSpeedModifyReturnValue(double original) {
        if (ModConfigs.faster_minecart) {

            if (this.blockPosition().equals(diversity$lastPos))
                return original * diversity$lastMaxSpeedMult;

            BlockState blockStateBelow = this.level().getBlockState(this.blockPosition().below());

            double multiplier = 1;
            if (Compare.isHasTag(blockStateBelow.getBlock(), "diversity:rail_supporter"))
                multiplier = 2;

            diversity$lastMaxSpeedMult = multiplier;
            diversity$lastPos = this.blockPosition();

            return original * multiplier;
        }

        return original;
    }
}
