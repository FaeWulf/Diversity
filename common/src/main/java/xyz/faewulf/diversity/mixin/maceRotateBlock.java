package xyz.faewulf.diversity.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.MaceItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import xyz.faewulf.diversity.util.config.ModConfigs;
import xyz.faewulf.diversity.util.getNextBlockState;

@Mixin(MaceItem.class)
public abstract class maceRotateBlock extends Item {
    public maceRotateBlock(Properties settings) {
        super(settings);
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {

        if (!ModConfigs.mace_rotate_block)
            return InteractionResult.PASS;

        if (context.getLevel().isClientSide)
            return InteractionResult.PASS;

        if (context.getPlayer() instanceof ServerPlayer serverPlayerEntity) {
            if (!serverPlayerEntity.isShiftKeyDown())
                return InteractionResult.PASS;

            BlockState currentBlock = context.getLevel().getBlockState(context.getClickedPos());
            BlockState blockState = getNextBlockState.getNextState(currentBlock);
            BlockPos blockPos = context.getClickedPos();

            if (blockState != null && blockState.canSurvive(context.getLevel(), blockPos)) {

                Vec3 hitPos = context.getClickLocation();

                context.getLevel().setBlockAndUpdate(blockPos, blockState);
                context.getLevel().playSound(null, hitPos.x, hitPos.y, hitPos.z, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundSource.PLAYERS, 0.3f, 0.6f);
                context.getLevel().playSound(null, hitPos.x, hitPos.y, hitPos.z, SoundEvents.ITEM_FRAME_ROTATE_ITEM, SoundSource.PLAYERS, 0.7f, 0.6f);

                context.getItemInHand().hurtAndBreak(1, serverPlayerEntity, LivingEntity.getSlotForHand(context.getHand()));

                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }
}
