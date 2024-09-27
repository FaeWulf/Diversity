package xyz.faewulf.diversity.event;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import xyz.faewulf.diversity.Constants;
import xyz.faewulf.diversity.feature.entity.PseudoBlockEntity.PseudoBlockEntities;
import xyz.faewulf.diversity.feature.entity.PseudoBlockEntity.PseudoBlockEntity;
import xyz.faewulf.diversity.util.config.ModConfigs;
import xyz.faewulf.diversity.util.pseudoBlockEntityUtil;

public class placeWetSpongeBlock {
    public static void run(Level world, Player player, BlockPos blockPos) {

        if (!ModConfigs.wet_sponge_dry_in_warm_biome)
            return;

        if (player == null)
            return;

        if (world.isClientSide)
            return;

        Block block = world.getBlockState(blockPos).getBlock();

        try {
            if (block == Blocks.WET_SPONGE && world instanceof ServerLevel serverWorld) {

                if (pseudoBlockEntityUtil.getBlockEntity(world, blockPos) != null)
                    return;

                Display.TextDisplay w = PseudoBlockEntities.WET_SPONGE.build(player.level());
                w.setPos(blockPos.getCenter());

                if (w instanceof PseudoBlockEntity pseudoBlockEntity) {
                    if (!pseudoBlockEntity.diversity_Multiloader$isBlockEntityAlreadyExist())
                        world.addFreshEntity(w);
                }
            }
        } catch (ClassCastException classCastException) {
            Constants.LOG.error(classCastException.getMessage());
        }

    }
}
