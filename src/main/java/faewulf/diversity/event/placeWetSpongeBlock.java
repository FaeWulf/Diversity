package faewulf.diversity.event;

import faewulf.diversity.Diversity;
import faewulf.diversity.callback.BlockPlacedCallback;
import faewulf.diversity.inter.ICustomPseudoEntityBlock;
import faewulf.diversity.util.ModConfigs;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class placeWetSpongeBlock {

    public static void run() {

        if (!ModConfigs.shulker_label)
            return;

        BlockPlacedCallback.EVENT.register(placeWetSpongeBlock::check);
    }

    private static void check(ItemPlacementContext context) {

        PlayerEntity playerEntity = context.getPlayer();

        if (playerEntity == null)
            return;

        if (playerEntity.getWorld().isClient)
            return;

        BlockPos blockPos = context.getBlockPos();
        Block block = playerEntity.getWorld().getBlockState(blockPos).getBlock();

        try {
            if (block == Blocks.WET_SPONGE && playerEntity.getWorld() instanceof ServerWorld serverWorld) {
                DisplayEntity.TextDisplayEntity w = new DisplayEntity.TextDisplayEntity(EntityType.TEXT_DISPLAY, playerEntity.getWorld());
                w.setPosition(blockPos.toCenterPos());

                ((ICustomPseudoEntityBlock) w).setMode("wetSponge");

                serverWorld.spawnEntity(w);
            }
        } catch (Exception e) {
            Diversity.LOGGER.error(e.getMessage());
        }

    }
}
