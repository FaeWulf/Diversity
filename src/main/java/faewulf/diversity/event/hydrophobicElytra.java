package faewulf.diversity.event;

import faewulf.diversity.util.ModConfigs;
import net.fabricmc.fabric.api.entity.event.v1.EntityElytraEvents;
import net.minecraft.server.network.ServerPlayerEntity;

public class hydrophobicElytra {
    public static void run() {

        if (!ModConfigs.hydrophobic_elytra)
            return;

        EntityElytraEvents.CUSTOM.register(((entity, tickElytra) -> {

            if (entity instanceof ServerPlayerEntity serverPlayerEntity) {
                if (serverPlayerEntity.isFallFlying() &&
                        (serverPlayerEntity.isInFluid() || serverPlayerEntity.isTouchingWaterOrRain())
                )
                    serverPlayerEntity.stopFallFlying();
            }
            return true;
        }));
    }
}
