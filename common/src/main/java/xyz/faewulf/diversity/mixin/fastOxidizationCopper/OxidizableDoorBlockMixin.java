package xyz.faewulf.diversity.mixin.fastOxidizationCopper;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.WeatheringCopperDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.faewulf.diversity.util.CopperUtils;
import xyz.faewulf.diversity.util.config.ModConfigs;

@Mixin(WeatheringCopperDoorBlock.class)
public abstract class OxidizableDoorBlockMixin extends DoorBlock implements WeatheringCopper {
    public OxidizableDoorBlockMixin(BlockSetType type, Properties settings) {
        super(type, settings);
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
