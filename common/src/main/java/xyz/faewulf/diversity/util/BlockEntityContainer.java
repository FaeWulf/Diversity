package xyz.faewulf.diversity.util;

import xyz.faewulf.diversity.mixin.clickThrough.ChestBlockMixin;
import xyz.faewulf.diversity.mixin.shulkerBoxLabel.ShulkerBoxBlockMixin;
import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityContainer {

    /**
     * This method use for trying to make @player open container in @pos
     *
     * @param pos    position of the target block
     * @param player target player will try open container
     * @return true if success open target container, or else return false
     */
    static public boolean tryOpenContainer(BlockPos pos, Player player) {
        BlockEntity blockEntity = player.level().getBlockEntity(pos);
        BlockState blockState = player.level().getBlockState(pos);
        Block block = blockState.getBlock();

        if (blockEntity == null || block == null)
            return false;

        //if contains container
        if (blockEntity instanceof MenuProvider namedScreenHandlerFactory) {

            //case for chest
            if (blockEntity instanceof ChestBlockEntity chestBlockEntity) {

                ChestBlock chestBlock = (ChestBlock) block;
                MenuProvider chestScreenHandler = ((ChestBlockMixin) chestBlock).invokerCreateScreenHandlerFactory(blockState, player.level(), pos);

                if (chestScreenHandler != null) {
                    //replica of default chest open event
                    player.openMenu(chestScreenHandler);
                    player.awardStat(Stats.CUSTOM.get(Stats.OPEN_CHEST));
                    PiglinAi.angerNearbyPiglins(player, true);

                    return true;
                }

                return false;
            }

            //case for ShulkerBox
            if (blockEntity instanceof ShulkerBoxBlockEntity shulkerBoxBlockEntity) {
                if (ShulkerBoxBlockMixin.invokeCanOpen(blockState, player.level(), pos, shulkerBoxBlockEntity)) {
                    player.openMenu(shulkerBoxBlockEntity);
                    player.awardStat(Stats.OPEN_SHULKER_BOX);
                    PiglinAi.angerNearbyPiglins(player, true);
                }

                return true;
            }


            //other case
            player.openMenu(namedScreenHandlerFactory);
            return true;
        }

        return false;
    }
}
