package xyz.faewulf.diversity.mixin.reversePhantom;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.levelgen.PhantomSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import xyz.faewulf.diversity.feature.player.sleepCounter.PlayerSleepStat;
import xyz.faewulf.diversity.util.config.ModConfigs;

@Mixin(PhantomSpawner.class)
public abstract class PhantomSpawnerMixin implements CustomSpawner {
    @ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;nextInt(I)I", ordinal = 1))
    private int tickModify(int original, @Local ServerPlayer serverPlayer) {

        if (ModConfigs.reverse_phantom) {
            if (serverPlayer instanceof PlayerSleepStat playerSleepStat) {
                return Math.clamp(playerSleepStat.diversity_Multiloader$getSleepStreak() * 24000L, 1, Integer.MAX_VALUE);
            }
        }

        return original;
    }
}
