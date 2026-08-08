package xyz.faewulf.diversity.mixin.curse.reversePhantom;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.faewulf.diversity.feature.player.sleepCounter.PlayerSleepStat;

@Mixin(ServerPlayer.class)
public abstract class PlayerMixin extends Player implements PlayerSleepStat {

    @Unique
    private int diversity_Multiloader$sleepStrike = 0;
    @Unique
    private int diversity_Multiloader$hasSleep = 0;

    public PlayerMixin(Level level, GameProfile gameProfile) {
        super(level, gameProfile);
    }

    @Shadow
    public abstract @NotNull ServerLevel level();

    //reset sleep strike stat
    @Inject(method = "die", at = @At("TAIL"))
    private void dieInject(DamageSource pCause, CallbackInfo ci) {
        this.diversity_Multiloader$sleepStrike = 0;
    }

    //add status if sleep
    @Inject(method = "stopSleepInBed", at = @At("TAIL"))
    private void stopSleepInBedInject(boolean pWakeImmediately, boolean pUpdateLevelForSleepingPlayers, CallbackInfo ci) {
        //if sleep
        if (!pWakeImmediately && !pUpdateLevelForSleepingPlayers) {
            this.diversity_Multiloader$sleepStrike++;
            this.diversity_Multiloader$hasSleep = 1;
        }
    }

    //reset status if skip sleep
    @Inject(method = "tick", at = @At("HEAD"))
    private void tickInject(CallbackInfo ci) {
        if (this.level().getOverworldClockTime() % 24000L == 20 && this.diversity_Multiloader$hasSleep == 0) {
            this.diversity_Multiloader$sleepStrike = 0;
        }

        if (this.level().getOverworldClockTime() % 24000L > 20) {
            this.diversity_Multiloader$hasSleep = 0;
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void saveDataInject(ValueOutput valueOutput, CallbackInfo ci) {
        valueOutput.putInt("diversity:sleep_strike", this.diversity_Multiloader$sleepStrike);
        valueOutput.putInt("diversity:has_sleep", this.diversity_Multiloader$hasSleep);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void loadDataInject(ValueInput valueInput, CallbackInfo ci) {
        this.diversity_Multiloader$sleepStrike = valueInput.getInt("diversity:sleep_strike").orElse(0);
        this.diversity_Multiloader$hasSleep = valueInput.getInt("diversity:has_sleep").orElse(0);
    }


    @Override
    public int diversity_Multiloader$getSleepStreak() {
        return diversity_Multiloader$sleepStrike;
    }

    @Override
    public void diversity_Multiloader$setSleepStreak(int value) {
        this.diversity_Multiloader$sleepStrike = value;
    }
}
