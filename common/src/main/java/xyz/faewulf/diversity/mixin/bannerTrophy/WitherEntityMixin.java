package xyz.faewulf.diversity.mixin.bannerTrophy;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PowerableMob;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.faewulf.diversity.util.CustomBanner;
import xyz.faewulf.diversity.util.config.ModConfigs;

@Mixin(WitherBoss.class)
public abstract class WitherEntityMixin extends Monster implements PowerableMob, RangedAttackMob {

    protected WitherEntityMixin(EntityType<? extends Monster> entityType, Level world) {
        super(entityType, world);
    }

    @Inject(method = "dropCustomDeathLoot", at = @At("TAIL"))
    private void dropEquipmentInject(DamageSource $$0, int $$1, boolean $$2, CallbackInfo ci) {

        if (!ModConfigs.banner_trohpy) return;

        ItemEntity itemEntity = this.spawnAtLocation(CustomBanner.witherBanner());
        if (itemEntity != null) {
            itemEntity.setExtendedLifetime();
        }
    }
}
