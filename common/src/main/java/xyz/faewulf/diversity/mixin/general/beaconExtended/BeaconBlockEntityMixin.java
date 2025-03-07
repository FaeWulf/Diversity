package xyz.faewulf.diversity.mixin.general.beaconExtended;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import xyz.faewulf.diversity.util.config.ModConfigs;

@Mixin(BeaconBlockEntity.class)
public abstract class BeaconBlockEntityMixin extends BlockEntity {
    public BeaconBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @ModifyExpressionValue(method = "updateBase", at = @At(value = "CONSTANT", args = "intValue=4", ordinal = 0))
    private static int modifyMaxLevelValue(int original) {

        if (ModConfigs.beacon_extended)
            return 10;

        return original;
    }

    @ModifyVariable(method = "applyEffects", at = @At("STORE"), ordinal = 0)
    private static double applyEffectModifyRangeValue(double d, @Local(argsOnly = true) int beaconLevel) {
        if (beaconLevel > 4 && ModConfigs.beacon_extended) {
            int extraLayer = beaconLevel - 4;
            d += extraLayer * 10;
        }
        return d;
    }
}
