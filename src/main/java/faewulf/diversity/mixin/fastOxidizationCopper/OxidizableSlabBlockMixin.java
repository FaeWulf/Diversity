package faewulf.diversity.mixin.fastOxidizationCopper;

import faewulf.diversity.util.CopperUtils;
import faewulf.diversity.util.ModConfigs;
import net.minecraft.block.BlockState;
import net.minecraft.block.Oxidizable;
import net.minecraft.block.OxidizableSlabBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OxidizableSlabBlock.class)
public abstract class OxidizableSlabBlockMixin extends SlabBlock implements Oxidizable {
    public OxidizableSlabBlockMixin(Settings settings) {
        super(settings);
    }


    @Inject(method = "randomTick", at = @At("HEAD"), cancellable = true)
    private void randomTickInject(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {

        if (!ModConfigs.faster_oxidization)
            return;

        if ((CopperUtils.isWaterNearby(pos, world) || world.isRaining()) && random.nextInt(10) == 1) {
            world.setBlockState(pos, CopperUtils.tryDegrade(this, state));
            ci.cancel();
        }
    }
}
