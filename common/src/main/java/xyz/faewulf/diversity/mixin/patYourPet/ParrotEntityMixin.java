package xyz.faewulf.diversity.mixin.patYourPet;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.faewulf.diversity.util.ModConfigs;

import java.util.Random;

@Mixin(Parrot.class)
public class ParrotEntityMixin {

    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    private void InjectInteractMod(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {

        if (!ModConfigs.pet_patting)
            return;

        TamableAnimal tameableEntity = (TamableAnimal) (Object) this;

        if (player.level().isClientSide)
            return;

        //not tamed
        if (!tameableEntity.isTame())
            return;

        //player ont sneaking or holding items
        if (!player.isShiftKeyDown() || !player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty())
            return;

        //if not owner
        if (!tameableEntity.isOwnedBy(player)) {
            if (new Random().nextInt(4) != 0) {
                tameableEntity.playSound(SoundEvents.PARROT_IMITATE_WARDEN, 0.5f, 1.0f);
                Vec3 Pos = tameableEntity.position();
                ((ServerLevel) player.level()).sendParticles(ParticleTypes.ANGRY_VILLAGER, Pos.x, Pos.y + 0.2, Pos.z, 3, 0.3, 0.2, 0.3, 0.5);
                return;
            }
        }

        tameableEntity.playSound(SoundEvents.PARROT_AMBIENT, 0.5f, 1.0f);

        Vec3 Pos = tameableEntity.position();
        ((ServerLevel) player.level()).sendParticles(ParticleTypes.HEART, Pos.x, Pos.y + 0.2, Pos.z, 3, 0.3, 0.2, 0.3, 0.5);

        cir.setReturnValue(InteractionResult.SUCCESS);
        cir.cancel();
    }
}
