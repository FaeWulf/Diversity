package faewulf.diversity.event;

import faewulf.diversity.util.ModConfigs;
import net.fabricmc.fabric.api.entity.event.v1.EntityElytraEvents;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.network.ServerPlayerEntity;

public class hydrophobicElytra {
    public static void run() {


        EntityElytraEvents.CUSTOM.register(((entity, tickElytra) -> {
            if (!ModConfigs.hydrophobic_elytra)
                return false;

            if (entity instanceof ServerPlayerEntity serverPlayerEntity) {
                if (serverPlayerEntity.isFallFlying() &&
                        (
                                //? >=1.21
                                /*serverPlayerEntity.isInFluid()*/
                                //? =1.20.1
                                serverPlayerEntity.isSubmergedIn(FluidTags.LAVA)
                                        || serverPlayerEntity.isTouchingWaterOrRain()
                        )
                ) {
                    serverPlayerEntity.stopFallFlying();
                    return true;
                }
            }
            return false;
        }));
    }
}
