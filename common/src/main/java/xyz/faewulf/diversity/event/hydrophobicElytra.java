package xyz.faewulf.diversity.event;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import xyz.faewulf.diversity.util.config.ModConfigs;

public class hydrophobicElytra {
    public static boolean run(Entity entity) {
        if (!ModConfigs.hydrophobic_elytra)
            return false;

        if (entity instanceof ServerPlayer serverPlayerEntity) {
            if (serverPlayerEntity.isFallFlying() &&
                    (serverPlayerEntity.isInLiquid() || serverPlayerEntity.isInWaterOrRain())
            ) {
                serverPlayerEntity.stopFallFlying();
                serverPlayerEntity.level().playSound(null, serverPlayerEntity.blockPosition(), SoundEvents.ARMOR_EQUIP_ELYTRA.value(), SoundSource.PLAYERS, 0.5f, 1.5f);
                return true;
            }
        }
        return false;
    }
}
