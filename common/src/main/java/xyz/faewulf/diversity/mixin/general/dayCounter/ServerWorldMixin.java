package xyz.faewulf.diversity.mixin.general.dayCounter;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraft.world.level.storage.WritableLevelData;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.faewulf.diversity.Constants;
import xyz.faewulf.diversity.util.config.ModConfigs;

import java.util.List;

@Mixin(ServerLevel.class)
public abstract class ServerWorldMixin extends Level implements WorldGenLevel {

    @Unique
    private long begin_time;

    @Unique
    private long end_time;

    @Unique
    private boolean diversity_Multiloader$beginAnnounce = false;

    protected ServerWorldMixin(WritableLevelData p_270739_, ResourceKey<Level> p_270683_, RegistryAccess p_270200_, Holder<DimensionType> p_270240_, boolean p_270904_, boolean p_270470_, long p_270248_, int p_270466_) {
        super(p_270739_, p_270683_, p_270200_, p_270240_, p_270904_, p_270470_, p_270248_, p_270466_);
    }

    @Shadow
    public abstract @NotNull List<ServerPlayer> players();

    @Shadow
    @Final
    private boolean tickTime;

    @Shadow
    @Final
    private ServerLevelData serverLevelData;

    @Shadow
    public abstract GameRules getGameRules();

    @Inject(method = "tickTime", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/storage/ServerLevelData;setGameTime(J)V"))
    private void tickTimeInject(CallbackInfo ci) {

        if (ModConfigs.day_counter <= 0 || !this.tickTime)
            return;

        if (!this.getGameRules().get(GameRules.ADVANCE_TIME)) {
            return;
        }

        if (this.dimensionType().hasSkyLight() && this.getOverworldClockTime() % ModConfigs.day_counter_tick_per_day == 0) {

            //per ... day
            if ((this.getOverworldClockTime() / ModConfigs.day_counter_tick_per_day + 1L) % ModConfigs.day_counter != 0)
                return;

            diversity_Multiloader$beginAnnounce = true;
            begin_time = this.getGameTime();
            //String message = "Day #" + (this.getOverworldClockTime() / ModConfigs.day_counter_tick_per_day + 1L) + " has arrived!";
            String message = ModConfigs.day_counter_message_holder.replaceAll("%s", String.valueOf((this.getOverworldClockTime() / ModConfigs.day_counter_tick_per_day + 1L)));
            end_time = begin_time + (long) message.length() * ModConfigs.day_counter_speed + 20 * 4;
        }

        diversity_Multiloader$announceNewDay();
    }

    @Unique
    private void diversity_Multiloader$announceNewDay() {
        if (!diversity_Multiloader$beginAnnounce)
            return;

        final long current_time = this.getGameTime();

        if (current_time > end_time) {
            diversity_Multiloader$beginAnnounce = false;
            return;
        }

        if ((current_time - begin_time) % ModConfigs.day_counter_speed == 0) {
            String message = ModConfigs.day_counter_message_holder.replaceAll("%s", String.valueOf((this.getOverworldClockTime() / ModConfigs.day_counter_tick_per_day + 1L)));
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

                player.sendSystemMessage(
                        Component
                                .literal(message.substring(0, cut_pos) + "_")
                                .withStyle(ChatFormatting.GOLD)
                                .withStyle(style -> style.withHoverEvent(
                                        new HoverEvent.ShowText(Component.literal(Constants.MOD_ID + "_day-counter"))
                                ))
                        ,
                        true
                );

                if (playSound)
                    //player.playNotifySound(SoundEvents.NOTE_BLOCK_HAT.value(), SoundSource.PLAYERS, 0.1f, 1.4f);
                    player.connection.send(new ClientboundSoundPacket(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.NOTE_BLOCK_HAT.value()), SoundSource.PLAYERS, player.getX(), player.getY(), player.getZ(), 0.1f, 1.4f, this.random.nextLong()));
            }
        }
    }
}
