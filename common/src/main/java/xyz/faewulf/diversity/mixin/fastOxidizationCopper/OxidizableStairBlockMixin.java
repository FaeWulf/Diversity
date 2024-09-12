package xyz.faewulf.diversity.mixin.fastOxidizationCopper;

import xyz.faewulf.diversity.util.CopperUtils;
import xyz.faewulf.diversity.util.config.ModConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.WeatheringCopperStairBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WeatheringCopperStairBlock.class)
public abstract class OxidizableStairBlockMixin extends StairBlock implements WeatheringCopper {

    public OxidizableStairBlockMixin(BlockState baseBlockState, Properties settings) {
        super(baseBlockState, settings);
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
