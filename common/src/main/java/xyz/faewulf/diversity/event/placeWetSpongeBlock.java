package xyz.faewulf.diversity.event;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import xyz.faewulf.diversity.Constants;
import xyz.faewulf.diversity.inter.ICustomPseudoEntityBlock;
import xyz.faewulf.diversity.util.ModConfigs;

import java.util.List;

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

                AABB box = new AABB(
                        blockPos.getX() + 0.3f, blockPos.getY() + 0.3f, blockPos.getZ() + 0.3f,
                        blockPos.getX() + 0.7f, blockPos.getY() + 0.7f, blockPos.getZ() + 0.7f
                );

                List<Entity> entitiesWithinRadius = world.getEntitiesOfClass(Entity.class, box, entity -> entity.getType() == EntityType.TEXT_DISPLAY);

                if (!entitiesWithinRadius.isEmpty())
                    return;

                Display.TextDisplay w = new Display.TextDisplay(EntityType.TEXT_DISPLAY, player.level());

                w.setPos(blockPos.getCenter());
                ((ICustomPseudoEntityBlock) w).setMode("wetSponge");

                serverWorld.addFreshEntity(w);
            }
        } catch (ClassCastException classCastException) {
            Constants.LOG.error(classCastException.getMessage());
            return;
        }

    }
}
