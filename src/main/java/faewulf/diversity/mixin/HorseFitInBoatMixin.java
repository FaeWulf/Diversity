package faewulf.diversity.mixin;

import faewulf.diversity.util.ModConfigs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.CamelEntity;
import net.minecraft.entity.passive.DonkeyEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.passive.MuleEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BoatEntity.class)
public class HorseFitInBoatMixin {

    //just modify return value of isSmallerThanBoat to true for horse/donkey and camel only
    @Inject(method = "isSmallerThanBoat", at = @At("RETURN"), cancellable = true)
    private void isSmallerThanBoatInject(Entity entity, CallbackInfoReturnable<Boolean> cir) {


        if (!ModConfigs.horse_can_seat_on_boat)
            return;

        if (entity instanceof HorseEntity || entity instanceof DonkeyEntity || entity instanceof MuleEntity || entity instanceof CamelEntity) {
            cir.setReturnValue(true);
            cir.cancel();
        }
    }
}
