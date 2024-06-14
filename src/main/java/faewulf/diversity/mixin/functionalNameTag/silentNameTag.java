package faewulf.diversity.mixin.functionalNameTag;

import faewulf.diversity.util.ModConfigs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.sound.SoundEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class silentNameTag {

    @Inject(method = "playSound", at = @At("HEAD"), cancellable = true)
    private void playSoud(SoundEvent sound, float volume, float pitch, CallbackInfo ci) {

        if (!ModConfigs.silent_nametag)
            return;

        if ((Object) this instanceof LivingEntity livingEntity) {
            if (livingEntity.getCustomName() == null)
                return;

            String name = livingEntity.getCustomName().getString();

            if (name.contains("silent") || name.contains("shutup")) {
                ci.cancel();
            }
        }


    }
}
