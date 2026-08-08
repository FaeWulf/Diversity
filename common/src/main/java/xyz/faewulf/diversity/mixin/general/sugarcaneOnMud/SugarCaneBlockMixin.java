package xyz.faewulf.diversity.mixin.general.sugarcaneOnMud;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SugarCaneBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.faewulf.diversity.util.config.ModConfigs;
import xyz.faewulf.lib.util.Compare;

@Mixin(SugarCaneBlock.class)
public abstract class SugarCaneBlockMixin extends Block {
    public SugarCaneBlockMixin(Properties properties) {
        super(properties);
    }


    @Inject(method = "canSurvive", at = @At("RETURN"), cancellable = true)
    private void canSurviveModifyResult(BlockState state, LevelReader level, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {

        if (!ModConfigs.mud_sugarcane)
            return;

        BlockState blockState = level.getBlockState(pos.below());

        if (Compare.isHasTag(blockState.getBlock(), "diversity:strong_support_sugarcane"))
            cir.setReturnValue(true);
    }
}
