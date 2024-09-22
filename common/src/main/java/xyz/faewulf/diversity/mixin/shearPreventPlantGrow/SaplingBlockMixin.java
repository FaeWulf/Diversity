package xyz.faewulf.diversity.mixin.shearPreventPlantGrow;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.faewulf.diversity.feature.entity.PseudoBlockEntity.PseudoBlockEntities;
import xyz.faewulf.diversity.feature.entity.PseudoBlockEntity.PseudoBlockEntity;
import xyz.faewulf.diversity.util.pseudoBlockEntityUtil;

import java.util.Objects;

@Mixin(SaplingBlock.class)
public abstract class SaplingBlockMixin extends Block {
    public SaplingBlockMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "randomTick", at = @At("HEAD"), cancellable = true)
    private void tickInject(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom, CallbackInfo ci) {
        PseudoBlockEntity pseudoBlockEntity = pseudoBlockEntityUtil.getBlockEntity(pLevel, pPos);

        if (pseudoBlockEntity == null || pseudoBlockEntity.getEntityType() == null)
            return;

        if (Objects.equals(pseudoBlockEntity.getEntityType(), PseudoBlockEntities.STOP_GROW.getDiversity_type())) {
            ci.cancel();
        }
    }
}
