package faewulf.diversity.event;

import faewulf.diversity.util.ModConfigs;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.CropBlock;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.server.world.ServerWorld;

public class breakCrops {

    public static void run() {
        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {

            if (!ModConfigs.xp_crops)
                return;

            if (world.isClient)
                return;

            if (world instanceof ServerWorld serverWorld) {
                if (state.getBlock() instanceof CropBlock cropBlock) {

                    int age = cropBlock.getAge(state);
                    int maxAge = cropBlock.getMaxAge();

                    if (age == maxAge)
                        ExperienceOrbEntity.spawn(serverWorld, pos.toCenterPos(), world.random.nextBetween(0, 1));
                }
            }
        });
    }

}
