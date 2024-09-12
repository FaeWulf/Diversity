package xyz.faewulf.diversity.mixin.functionalNameTag;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.faewulf.diversity.util.config.ModConfigs;

@Mixin(Entity.class)
public class silentNameTag {

    @Inject(method = "playSound(Lnet/minecraft/sounds/SoundEvent;FF)V", at = @At("HEAD"), cancellable = true)
    private void playSoud(SoundEvent sound, float volume, float pitch, CallbackInfo ci) {

        if (!ModConfigs.silent_nametag)
            return;

        if ((Object) this instanceof LivingEntity livingEntity) {
            if (livingEntity.getCustomName() == null)
                return;

            String name = livingEntity.getCustomName().getString().toLowerCase();

            if (name.contains("silent") || name.contains("shutup")) {
                ci.cancel();
            }
        }


    }
}
