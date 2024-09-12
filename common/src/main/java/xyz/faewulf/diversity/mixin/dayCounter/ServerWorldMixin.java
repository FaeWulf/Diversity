package xyz.faewulf.diversity.mixin.dayCounter;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.faewulf.diversity.util.config.ModConfigs;

import java.util.List;
import java.util.function.Supplier;

@Mixin(ServerLevel.class)
public abstract class ServerWorldMixin extends Level implements WorldGenLevel {

    @Unique
    private long begin_time;

    @Unique
    private long end_time;

    @Unique
    private boolean beginAnnounce = false;

    @Shadow
    public abstract List<ServerPlayer> players();

    @Shadow
    public abstract void playSeededSound(@Nullable Player source, double x, double y, double z, Holder<SoundEvent> sound, SoundSource category, float volume, float pitch, long seed);

    protected ServerWorldMixin(WritableLevelData properties, ResourceKey<Level> registryRef, RegistryAccess registryManager, Holder<DimensionType> dimensionEntry, Supplier<ProfilerFiller> profiler, boolean isClient, boolean debugWorld, long biomeAccess, int maxChainedNeighborUpdates) {
        super(properties, registryRef, registryManager, dimensionEntry, profiler, isClient, debugWorld, biomeAccess, maxChainedNeighborUpdates);
    }

    @Inject(method = "tickTime", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;setDayTime(J)V"))
    private void tickTimeInject(CallbackInfo ci) {

        if (!ModConfigs.day_counter)
            return;

        if (this.dimensionType().hasSkyLight() && this.getDayTime() % 24000L == 0) {
            beginAnnounce = true;
            begin_time = this.getDayTime();
            String message = "Day #" + (this.getDayTime() / 24000L + 1L) + " has arrived!";
            end_time = begin_time + message.length() * 2 + 20 * 4;
        }

        announceNewDay();
    }

    @Unique
    private void announceNewDay() {
        if (!beginAnnounce)
            return;

        final long current_time = this.getDayTime();

        if (current_time > end_time) {
            beginAnnounce = false;
            return;
        }

        if ((current_time - begin_time) % 2 == 0) {
            String message = "Day #" + (current_time / 24000L + 1L) + " has arrived!";
            for (ServerPlayer player : this.players()) {

                boolean playSound = true;
                int cut_pos = (int) ((current_time - begin_time) / 2);

                if (cut_pos > message.length()) {
                    cut_pos = message.length();
                    playSound = false;
                }

                player.displayClientMessage(Component.literal(message.substring(0, cut_pos) + "_").withStyle(ChatFormatting.GOLD), true);

                if (playSound)
                    player.playNotifySound(SoundEvents.NOTE_BLOCK_HAT.value(), SoundSource.PLAYERS, 0.4f, 1.4f * (this.random.nextFloat() * 0.2f + 0.9f));
            }
        }
    }
}
