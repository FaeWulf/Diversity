package faewulf.diversity.mixin.dayCounter;

import faewulf.diversity.util.ModConfigs;
import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.Supplier;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin extends World implements StructureWorldAccess, AttachmentTarget {

    @Unique
    private long begin_time;

    @Unique
    private long end_time;

    @Unique
    private boolean beginAnnounce = false;

    @Shadow
    public abstract List<ServerPlayerEntity> getPlayers();

    @Shadow
    public abstract void playSound(@Nullable PlayerEntity source, double x, double y, double z, RegistryEntry<SoundEvent> sound, SoundCategory category, float volume, float pitch, long seed);

    protected ServerWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, DynamicRegistryManager registryManager, RegistryEntry<DimensionType> dimensionEntry, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long biomeAccess, int maxChainedNeighborUpdates) {
        super(properties, registryRef, registryManager, dimensionEntry, profiler, isClient, debugWorld, biomeAccess, maxChainedNeighborUpdates);
    }

    @Inject(method = "tickTime", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;setTimeOfDay(J)V"))
    private void tickTimeInject(CallbackInfo ci) {

        if (!ModConfigs.day_counter)
            return;

        if (this.getDimension().hasSkyLight() && this.getTimeOfDay() % 24000L == 0) {
            beginAnnounce = true;
            begin_time = this.getTimeOfDay();
            String message = "Day #" + (this.getTimeOfDay() / 24000L + 1L) + " has arrived!";
            end_time = begin_time + message.length() * 3 + 20 * 4;
        }

        announceNewDay();
    }

    @Unique
    private void announceNewDay() {
        if (!beginAnnounce)
            return;

        final long current_time = this.getTimeOfDay();

        if (current_time > end_time) {
            beginAnnounce = false;
            return;
        }

        if ((current_time - begin_time) % 3 == 0) {
            String message = "Day #" + (current_time / 24000L + 1L) + " has arrived!";
            for (ServerPlayerEntity player : this.getPlayers()) {

                boolean playSound = true;
                int cut_pos = (int) ((current_time - begin_time) / 3);

                if (cut_pos > message.length()) {
                    cut_pos = message.length();
                    playSound = false;
                }

                player.sendMessage(Text.literal(message.substring(0, cut_pos) + "_").formatted(Formatting.GOLD), true);

                if (playSound) {
                    //? if >=1.21 {
                    /*player.playSoundToPlayer(SoundEvents.BLOCK_NOTE_BLOCK_HAT.value(), SoundCategory.PLAYERS, 0.5f, 1.4f);
                     *///?}

                    //? if =1.20.1 {
                    player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_HAT.value(), SoundCategory.PLAYERS, 0.5f, 1.4f);
                    //?}
                }

            }
        }
    }
}
