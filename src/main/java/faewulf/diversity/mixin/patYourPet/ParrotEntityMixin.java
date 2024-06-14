package faewulf.diversity.mixin.patYourPet;

import faewulf.diversity.util.ModConfigs;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(ParrotEntity.class)
public class ParrotEntityMixin {

    @Inject(method = "interactMob", at = @At("HEAD"), cancellable = true)
    private void InjectInteractMod(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {

        if (!ModConfigs.pet_patting)
            return;

        TameableEntity tameableEntity = (TameableEntity) (Object) this;

        if (player.getWorld().isClient)
            return;

        //not tamed
        if (!tameableEntity.isTamed())
            return;

        //player ont sneaking or holding items
        if (!player.isSneaking() || !player.getStackInHand(Hand.MAIN_HAND).isEmpty())
            return;

        //if not owner
        if (!tameableEntity.isOwner(player)) {
            if (new Random().nextInt(4) != 0) {
                tameableEntity.playSound(SoundEvents.ENTITY_PARROT_IMITATE_WARDEN, 0.5f, 1.0f);
                Vec3d Pos = tameableEntity.getPos();
                ((ServerWorld) player.getWorld()).spawnParticles(ParticleTypes.ANGRY_VILLAGER, Pos.x, Pos.y + 0.2, Pos.z, 3, 0.3, 0.2, 0.3, 0.5);
                return;
            }
        }

        tameableEntity.playSound(SoundEvents.ENTITY_PARROT_AMBIENT, 0.5f, 1.0f);

        Vec3d Pos = tameableEntity.getPos();
        ((ServerWorld) player.getWorld()).spawnParticles(ParticleTypes.HEART, Pos.x, Pos.y + 0.2, Pos.z, 3, 0.3, 0.2, 0.3, 0.5);

        cir.setReturnValue(ActionResult.SUCCESS);
        cir.cancel();
    }
}
