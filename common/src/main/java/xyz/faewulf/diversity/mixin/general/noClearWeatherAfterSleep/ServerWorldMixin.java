package xyz.faewulf.diversity.mixin.general.noClearWeatherAfterSleep;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.WeatherData;
import net.minecraft.world.level.storage.ServerLevelData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.faewulf.diversity.util.config.ModConfigs;

@Mixin(value = ServerLevel.class, priority = 1100)
public abstract class ServerWorldMixin {

    @Shadow
    @Final
    private ServerLevelData serverLevelData;

    @Shadow
    public abstract MinecraftServer getServer();

    @Shadow
    public abstract WeatherData getWeatherData();

    @Inject(method = "resetWeatherCycle", at = @At("HEAD"), cancellable = true)
    private void resetWeatherMixin(CallbackInfo ci) {

        if (ModConfigs.sleep_dont_skip_weather == ModConfigs.weatherType.DISABLE)
            return;

        //if option is don't skip thunder as well
        if (ModConfigs.sleep_dont_skip_weather == ModConfigs.weatherType.ALL_WEATHER)
            ci.cancel();

        //normal mode: 1 (don't skip rain only)
        this.getWeatherData().setThunderTime(0);
        this.getWeatherData().setThundering(false);
        ci.cancel();
    }

}
