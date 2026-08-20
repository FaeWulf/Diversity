package xyz.faewulf.diversity.event;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import xyz.faewulf.diversity.util.config.ModConfigs;

public class xpCrops {

    public static void run(Level serverLevel, Player player, BlockPos pos, BlockState state) {
        if (!ModConfigs.xp_crops)
            return;

        if (serverLevel.isClientSide())
            return;

        if (serverLevel instanceof ServerLevel serverWorld) {
            if (state.getBlock() instanceof CropBlock cropBlock) {
                if (cropBlock.isMaxAge(state))
                    ExperienceOrb.award(serverWorld, Vec3.atCenterOf(pos), serverWorld.getRandom().nextIntBetweenInclusive(0, 1));
            }
        }
    }
}
