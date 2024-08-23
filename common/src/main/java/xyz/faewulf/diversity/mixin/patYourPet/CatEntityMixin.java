package xyz.faewulf.diversity.mixin.patYourPet;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.faewulf.diversity.util.ModConfigs;

import java.util.Random;

@Mixin(Cat.class)
public abstract class CatEntityMixin {

    @Shadow
    public abstract void hiss();

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
                this.hiss();
                Vec3 catPos = tameableEntity.position();
                ((ServerLevel) player.level()).sendParticles(ParticleTypes.ANGRY_VILLAGER, catPos.x, catPos.y + 0.2, catPos.z, 3, 0.3, 0.2, 0.3, 0.5);
                return;
            }
        }

        if (new Random().nextInt(4) == 0)
            tameableEntity.playSound(SoundEvents.CAT_PURREOW, 0.5f, 1.0f);
        else
            tameableEntity.playSound(SoundEvents.CAT_PURR, 0.5f, 1.0f);

        Vec3 catPos = tameableEntity.position();
        ((ServerLevel) player.level()).sendParticles(ParticleTypes.HEART, catPos.x, catPos.y + 0.2, catPos.z, 3, 0.3, 0.2, 0.3, 0.5);

        cir.setReturnValue(InteractionResult.SUCCESS);
        cir.cancel();
    }
}