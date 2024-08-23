package xyz.faewulf.diversity.mixin.noClearWeatherAfterSleep;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.ServerLevelData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.faewulf.diversity.util.ModConfigs;

@Mixin(value = ServerLevel.class, priority = 1100)
public class ServerWorldMixin {

    @Shadow
    @Final
    private ServerLevelData serverLevelData;

    @Inject(method = "resetWeatherCycle", at = @At("HEAD"), cancellable = true)
    private void resetWeatherMixin(CallbackInfo ci) {

        if (ModConfigs.sleep_dont_skip_weather == ModConfigs.weatherType.DISABLE)
            return;

        //if option is don't skip thunder as well
        if (ModConfigs.sleep_dont_skip_weather == ModConfigs.weatherType.ALL_WEATHER)
            ci.cancel();

        //normal mode: 1 (don't skip rain only)
        this.serverLevelData.setThunderTime(0);
        this.serverLevelData.setThundering(false);
        ci.cancel();
    }

}
