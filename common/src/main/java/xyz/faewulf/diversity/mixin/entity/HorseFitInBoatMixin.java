package xyz.faewulf.diversity.mixin.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.animal.equine.Donkey;
import net.minecraft.world.entity.animal.equine.Horse;
import net.minecraft.world.entity.animal.equine.Mule;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.faewulf.diversity.util.config.ModConfigs;

@Mixin(AbstractBoat.class)
public class HorseFitInBoatMixin {

    //just modify return value of isSmallerThanBoat to true for horse/donkey and camel only
    @Inject(method = "hasEnoughSpaceFor", at = @At("RETURN"), cancellable = true)
    private void isSmallerThanBoatInject(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (!ModConfigs.horse_can_seat_on_boat)
            return;

        if (entity instanceof Horse || entity instanceof Donkey || entity instanceof Mule || entity instanceof Camel) {
            cir.setReturnValue(true);
            cir.cancel();
        }
    }
}
