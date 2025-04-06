package xyz.faewulf.diversity.mixin.general.dayCounter;

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
import org.jetbrains.annotations.NotNull;
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
    private boolean diversity_Multiloader$beginAnnounce = false;

    protected ServerWorldMixin(WritableLevelData properties, ResourceKey<Level> registryRef, RegistryAccess registryManager, Holder<DimensionType> dimensionEntry, Supplier<ProfilerFiller> profiler, boolean isClient, boolean debugWorld, long biomeAccess, int maxChainedNeighborUpdates) {
        super(properties, registryRef, registryManager, dimensionEntry, profiler, isClient, debugWorld, biomeAccess, maxChainedNeighborUpdates);
    }

    @Shadow
    public abstract @NotNull List<ServerPlayer> players();

    @Inject(method = "tickTime", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;setDayTime(J)V"))
    private void tickTimeInject(CallbackInfo ci) {

        if (ModConfigs.day_counter <= 0)
            return;

        if (this.dimensionType().hasSkyLight() && this.getDayTime() % ModConfigs.day_counter_tick_per_day == 0) {

            //per ... day
            if ((this.getDayTime() / ModConfigs.day_counter_tick_per_day + 1L) % ModConfigs.day_counter != 0)
                return;

            diversity_Multiloader$beginAnnounce = true;
            begin_time = this.getDayTime();
            String message = "Day #" + (this.getDayTime() / ModConfigs.day_counter_tick_per_day + 1L) + " has arrived!";
            end_time = begin_time + (long) message.length() * ModConfigs.day_counter_speed + 20 * 4;
        }

        diversity_Multiloader$announceNewDay();
    }

    @Unique
    private void diversity_Multiloader$announceNewDay() {
        if (!diversity_Multiloader$beginAnnounce)
            return;

        final long current_time = this.getDayTime();

        if (current_time > end_time) {
            diversity_Multiloader$beginAnnounce = false;
            return;
        }

        if ((current_time - begin_time) % ModConfigs.day_counter_speed == 0) {
            String message = "Day #" + (current_time / ModConfigs.day_counter_tick_per_day + 1L) + " has arrived!";
            for (ServerPlayer player : this.players()) {

                boolean playSound = true;
                int cut_pos = (int) ((current_time - begin_time) / ModConfigs.day_counter_speed);

                if (cut_pos < 0) {
                    diversity_Multiloader$beginAnnounce = false;
                    return;
                }

                if (cut_pos > message.length()) {
                    cut_pos = message.length();
                    playSound = false;
                }

                player.displayClientMessage(Component.literal(message.substring(0, cut_pos) + "_").withStyle(ChatFormatting.GOLD), true);

                if (playSound)
                    player.playNotifySound(SoundEvents.NOTE_BLOCK_HAT.value(), SoundSource.PLAYERS, 0.5f, 1.4f);
            }
        }
    }
}
