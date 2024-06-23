package faewulf.diversity.mixin.bannerTrophy;

import faewulf.diversity.util.CustomBanner;
import faewulf.diversity.util.ModConfigs;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.SkinOverlayOwner;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WitherEntity.class)
public abstract class WitherEntityMixin extends HostileEntity implements SkinOverlayOwner, RangedAttackMob {

    protected WitherEntityMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "dropEquipment", at = @At("TAIL"))
    private void dropEquipmentInject(ServerWorld world, DamageSource source, boolean causedByPlayer, CallbackInfo ci) {

        if (!ModConfigs.banner_trohpy) return;

        ItemEntity itemEntity = this.dropStack(CustomBanner.witherBanner(this.getRegistryManager().getWrapperOrThrow(RegistryKeys.BANNER_PATTERN)));
        if (itemEntity != null) {
            itemEntity.setCovetedItem();
        }
    }
}
