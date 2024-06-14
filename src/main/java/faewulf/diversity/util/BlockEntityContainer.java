package faewulf.diversity.util;

import faewulf.diversity.mixin.clickThrough.ChestBlockMixin;
import faewulf.diversity.mixin.shulkerBoxLabel.ShulkerBoxBlockMixin;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.stat.Stats;
import net.minecraft.util.math.BlockPos;

public class BlockEntityContainer {

    /**
     * This method use for trying to make @player open container in @pos
     *
     * @param pos    position of the target block
     * @param player target player will try open container
     * @return true if success open target container, or else return false
     */
    static public boolean tryOpenContainer(BlockPos pos, PlayerEntity player) {
        BlockEntity blockEntity = player.getWorld().getBlockEntity(pos);
        BlockState blockState = player.getWorld().getBlockState(pos);
        Block block = blockState.getBlock();

        if (blockEntity == null || block == null)
            return false;

        //if contains container
        if (blockEntity instanceof NamedScreenHandlerFactory namedScreenHandlerFactory) {

            //case for chest
            if (blockEntity instanceof ChestBlockEntity chestBlockEntity) {

                ChestBlock chestBlock = (ChestBlock) block;
                NamedScreenHandlerFactory chestScreenHandler = ((ChestBlockMixin) chestBlock).invokerCreateScreenHandlerFactory(blockState, player.getWorld(), pos);

                if (chestScreenHandler != null) {
                    //replica of default chest open event
                    player.openHandledScreen(chestScreenHandler);
                    player.incrementStat(Stats.CUSTOM.getOrCreateStat(Stats.OPEN_CHEST));
                    PiglinBrain.onGuardedBlockInteracted(player, true);

                    return true;
                }

                return false;
            }

            //case for ShulkerBox
            if (blockEntity instanceof ShulkerBoxBlockEntity shulkerBoxBlockEntity) {
                if (ShulkerBoxBlockMixin.invokeCanOpen(blockState, player.getWorld(), pos, shulkerBoxBlockEntity)) {
                    player.openHandledScreen(shulkerBoxBlockEntity);
                    player.incrementStat(Stats.OPEN_SHULKER_BOX);
                    PiglinBrain.onGuardedBlockInteracted(player, true);
                }

                return true;
            }


            //other case
            player.openHandledScreen(namedScreenHandlerFactory);
            return true;
        }

        return false;
    }
}
