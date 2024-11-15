package xyz.faewulf.diversity.mixin.general.fasterMineCart;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.VehicleEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import xyz.faewulf.diversity.util.compare;
import xyz.faewulf.diversity.util.config.ModConfigs;

@Mixin(AbstractMinecart.class)
public abstract class AbstractMinecartMixin extends VehicleEntity {
    public AbstractMinecartMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Unique
    private BlockPos diversity_Multiloader$lastPos;

    @Unique
    private double diversity_Multiloader$lastMaxSpeedMult = 1;

    @ModifyReturnValue(method = "getMaxSpeed", at = @At("RETURN"))
    private double getMaxSpeedModifyReturnValue(double original) {
        if (ModConfigs.faster_minecart) {

            if (this.blockPosition().equals(diversity_Multiloader$lastPos))
                return original * diversity_Multiloader$lastMaxSpeedMult;

            BlockState blockStateBelow = this.level().getBlockState(this.blockPosition().below());

            double multiplier = 1;
            if (compare.isHasTag(blockStateBelow.getBlock(), "diversity:rail_supporter"))
                multiplier = 2;

            diversity_Multiloader$lastMaxSpeedMult = multiplier;
            diversity_Multiloader$lastPos = this.blockPosition();

            return original * multiplier;
        }

        return original;
    }
}
