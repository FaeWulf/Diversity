package faewulf.diversity.mixin.bannerTrophy;

import faewulf.diversity.util.CustomBanner;
import faewulf.diversity.util.ModConfigs;
import net.minecraft.entity.Attackable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class WardenSuperClassMixin extends Entity implements Attackable {

    public WardenSuperClassMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "onDeath", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/damage/DamageSource;getAttacker()Lnet/minecraft/entity/Entity;"))
    private void onDeathInject(DamageSource damageSource, CallbackInfo ci) {

        if (!ModConfigs.banner_trohpy) return;

        if ((Object) this instanceof WardenEntity wardenEntity) {
            ItemStack wardenBanner = CustomBanner.wardenBanner(wardenEntity.getRegistryManager().getWrapperOrThrow(RegistryKeys.BANNER_PATTERN));
            wardenBanner.setCount(1);
            wardenEntity.dropStack(wardenBanner);
        }
    }
}
