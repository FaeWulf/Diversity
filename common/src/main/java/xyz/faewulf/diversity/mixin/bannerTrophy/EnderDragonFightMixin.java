package xyz.faewulf.diversity.mixin.bannerTrophy;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.dimension.end.EndDragonFight;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.EndPodiumFeature;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.faewulf.diversity.util.CustomBanner;
import xyz.faewulf.diversity.util.config.ModConfigs;

@Mixin(EndDragonFight.class)
public class EnderDragonFightMixin {

    @Shadow
    @Final
    private ServerLevel level;

    @Shadow
    @Final
    private BlockPos origin;

    //first dragon only
    @Inject(method = "setDragonKilled", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;setBlockAndUpdate(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Z"))
    private void dragonKilledInject1(EnderDragon dragon, CallbackInfo ci) {

        if (!ModConfigs.banner_trohpy) return;

        Vec3 pos = this.level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, EndPodiumFeature.getLocation(this.origin)).getCenter();

        ItemStack eggTrophy = CustomBanner.enderEggBanner();
        eggTrophy.setCount(1);
        ItemEntity itemEntity = new ItemEntity(level, pos.x(), pos.y() + 1, pos.z(), eggTrophy);
        itemEntity.setDeltaMovement(Vec3.ZERO);
        level.addFreshEntity(itemEntity);
    }

    @Inject(method = "setDragonKilled", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/dimension/end/EndDragonFight;spawnNewGateway()V"))
    private void dragonKilledInject2(EnderDragon dragon, CallbackInfo ci) {

        if (!ModConfigs.banner_trohpy) return;

        Vec3 pos = this.level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, EndPodiumFeature.getLocation(this.origin)).getCenter();

        ItemStack eggTrophy = CustomBanner.enderDragonBanner();
        eggTrophy.setCount(1);
        ItemEntity itemEntity = new ItemEntity(level, pos.x(), pos.y() + 1, pos.z(), eggTrophy);
        itemEntity.setDeltaMovement(Vec3.ZERO);
        level.addFreshEntity(itemEntity);
    }

}
