package faewulf.diversity.mixin.noClearWeatherAfterSleep;

import faewulf.diversity.util.ModConfigs;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public abstract class WorldMixin {

    @Shadow
    public abstract long getTimeOfDay();

    @Shadow
    public abstract BrewingRecipeRegistry getBrewingRecipeRegistry();

    @Inject(method = "isDay", at = @At("RETURN"), cancellable = true)
    private void isDayInject(CallbackInfoReturnable<Boolean> cir) {

        //modify sleep behavior only for mode 2
        if (ModConfigs.sleep_dont_skip_weather != ModConfigs.weatherType.ALL_WEATHER)
            return;

        //if night
        if (this.getTimeOfDay() < 24000 && this.getTimeOfDay() >= 12000) {
            cir.setReturnValue(false);
            cir.cancel();
        } else {
            cir.setReturnValue(true);
            cir.cancel();
        }
    }
}
