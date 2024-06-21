package faewulf.diversity.mixin.bannerTrophy;

import faewulf.diversity.util.CustomBanner;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonFight;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.feature.EndPortalFeature;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnderDragonFight.class)
public class EnderDragonFightMixin {

    @Shadow
    @Final
    private ServerWorld world;

    @Shadow
    @Final
    private BlockPos origin;

    //first dragon only
    @Inject(method = "dragonKilled", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)Z"))
    private void dragonKilledInject1(EnderDragonEntity dragon, CallbackInfo ci) {
        Vec3d pos = this.world.getTopPosition(Heightmap.Type.MOTION_BLOCKING, EndPortalFeature.offsetOrigin(this.origin)).toCenterPos();

        ItemStack eggTrophy = CustomBanner.enderEggBanner(dragon.getRegistryManager().getWrapperOrThrow(RegistryKeys.BANNER_PATTERN));
        eggTrophy.setCount(1);
        ItemEntity itemEntity = new ItemEntity(world, pos.getX(), pos.getY() + 1, pos.getZ(), eggTrophy);
        itemEntity.setVelocity(Vec3d.ZERO);
        world.spawnEntity(itemEntity);
    }

    @Inject(method = "dragonKilled", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/boss/dragon/EnderDragonFight;generateNewEndGateway()V"))
    private void dragonKilledInject2(EnderDragonEntity dragon, CallbackInfo ci) {
        Vec3d pos = this.world.getTopPosition(Heightmap.Type.MOTION_BLOCKING, EndPortalFeature.offsetOrigin(this.origin)).toCenterPos();

        ItemStack eggTrophy = CustomBanner.enderDragonBanner(dragon.getRegistryManager().getWrapperOrThrow(RegistryKeys.BANNER_PATTERN));
        eggTrophy.setCount(1);
        ItemEntity itemEntity = new ItemEntity(world, pos.getX(), pos.getY() + 1, pos.getZ(), eggTrophy);
        itemEntity.setVelocity(Vec3d.ZERO);
        world.spawnEntity(itemEntity);
    }

}
