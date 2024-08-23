package xyz.faewulf.diversity.event;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import xyz.faewulf.diversity.util.ModConfigs;

public class breakCrops {

    public static void run(Level serverLevel, Player player, BlockPos pos, BlockState state) {
        if (!ModConfigs.xp_crops)
            return;

        if (serverLevel.isClientSide)
            return;

        if (serverLevel instanceof ServerLevel serverWorld) {
            if (state.getBlock() instanceof CropBlock cropBlock) {

                int age = cropBlock.getAge(state);
                int maxAge = cropBlock.getMaxAge();

                if (age == maxAge)
                    ExperienceOrb.award(serverWorld, pos.getCenter(), serverWorld.random.nextIntBetweenInclusive(0, 1));
            }
        }
    }
}
