package xyz.faewulf.diversity.mixin.general.xpCrops;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.faewulf.diversity.util.config.ModConfigs;

@Mixin(Block.class)
public abstract class BlockMixin extends BlockBehaviour implements ItemLike {
    public BlockMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "dropResources(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/BlockEntity;)V", at = @At("HEAD"))
    private static void popResourceInject(BlockState state, LevelAccessor level, BlockPos pos, BlockEntity blockEntity, CallbackInfo ci) {
        if (!ModConfigs.xp_crops)
            return;
        if (level instanceof ServerLevel serverWorld) {
            if (state.getBlock() instanceof CropBlock cropBlock) {
                if (cropBlock.isMaxAge(state))
                    ExperienceOrb.award(serverWorld, Vec3.atCenterOf(pos), serverWorld.getRandom().nextIntBetweenInclusive(0, 1));
            }
        }
    }
}
