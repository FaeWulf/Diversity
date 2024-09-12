package xyz.faewulf.diversity.mixin.noClearWeatherAfterSleep;

import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.faewulf.diversity.util.config.ModConfigs;

@Mixin(Level.class)
public abstract class WorldMixin {

    @Shadow
    public abstract long getDayTime();

    @Inject(method = "isDay", at = @At("RETURN"), cancellable = true)
    private void isDayInject(CallbackInfoReturnable<Boolean> cir) {

        //modify sleep behavior only for mode 2
        if (ModConfigs.sleep_dont_skip_weather != ModConfigs.weatherType.ALL_WEATHER)
            return;

        //if night
        if (this.getDayTime() < 24000 && this.getDayTime() >= 12000) {
            cir.setReturnValue(false);
            cir.cancel();
        } else {
            cir.setReturnValue(true);
            cir.cancel();
        }
    }
}
