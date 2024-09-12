package xyz.faewulf.diversity.mixin.bannerTrophy;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Attackable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.ElderGuardian;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.faewulf.diversity.util.CustomBanner;
import xyz.faewulf.diversity.util.config.ModConfigs;

@Mixin(LivingEntity.class)
public abstract class ElderGuardianSuperClassMixin extends Entity implements Attackable {


    public ElderGuardianSuperClassMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Inject(method = "die", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/damagesource/DamageSource;getEntity()Lnet/minecraft/world/entity/Entity;"))
    private void onDeathInject(DamageSource damageSource, CallbackInfo ci) {

        if (!ModConfigs.banner_trohpy) return;

        if ((Object) this instanceof ElderGuardian elderGuardianEntity) {
            ItemStack wardenBanner = CustomBanner.elderGuardianBanner();
            wardenBanner.setCount(1);
            elderGuardianEntity.spawnAtLocation(wardenBanner);
        }
    }
}

