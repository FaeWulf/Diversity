package xyz.faewulf.diversity.mixinClient.general.dayCounter;

import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.faewulf.diversity.util.config.ModConfigs;

@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin {
    @Inject(method = "handleSoundEvent", at = @At("HEAD"), cancellable = true)
    private void handleSoundEventCancelDayCounterSoundInject(ClientboundSoundPacket packet, CallbackInfo ci) {

        if (!ModConfigs.blacklist_day_counter)
            return;

        float epsilon = Float.MIN_NORMAL;
        // Specific packet for day counter
        if (packet.getSource() == SoundSource.PLAYERS
                && packet.getSound() == SoundEvents.NOTE_BLOCK_HAT
                && Math.abs(packet.getVolume() - 0.1f) < epsilon
                && Math.abs(packet.getPitch() - 1.4f) < epsilon
        ) {
            ci.cancel();
        }
    }
}
