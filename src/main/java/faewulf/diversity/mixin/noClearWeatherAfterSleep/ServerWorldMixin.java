package faewulf.diversity.mixin.noClearWeatherAfterSleep;

import faewulf.diversity.util.ModConfigs;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.level.ServerWorldProperties;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ServerWorld.class, priority = 1100)
public class ServerWorldMixin {

    @Shadow
    @Final
    private ServerWorldProperties worldProperties;

    @Inject(method = "resetWeather", at = @At("HEAD"), cancellable = true)
    private void resetWeatherMixin(CallbackInfo ci) {

        if (!ModConfigs.sleep_dont_skip_weather)
            return;

        this.worldProperties.setThunderTime(0);
        this.worldProperties.setThundering(false);
        ci.cancel();
    }

}
