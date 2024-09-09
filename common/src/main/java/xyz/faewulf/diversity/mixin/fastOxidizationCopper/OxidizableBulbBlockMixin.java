package xyz.faewulf.diversity.mixin.fastOxidizationCopper;

import xyz.faewulf.diversity.util.CopperUtils;
import xyz.faewulf.diversity.util.config.ModConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.CopperBulbBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.WeatheringCopperBulbBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WeatheringCopperBulbBlock.class)
public abstract class OxidizableBulbBlockMixin extends CopperBulbBlock implements WeatheringCopper {
    public OxidizableBulbBlockMixin(Properties settings) {
        super(settings);
    }

    @Inject(method = "randomTick", at = @At("HEAD"), cancellable = true)
    private void randomTickInject(BlockState state, ServerLevel world, BlockPos pos, RandomSource random, CallbackInfo ci) {

        if (!ModConfigs.faster_oxidization)
            return;

        if ((CopperUtils.isWaterNearby(pos, world) || world.isRaining()) && random.nextInt(10) == 1) {
            world.setBlockAndUpdate(pos, CopperUtils.tryDegrade(this, state));
            ci.cancel();
        }
    }
}
