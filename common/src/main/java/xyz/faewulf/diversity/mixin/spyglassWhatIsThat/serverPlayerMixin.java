package xyz.faewulf.diversity.mixin.spyglassWhatIsThat;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.faewulf.diversity.util.config.ModConfigs;
import xyz.faewulf.diversity.util.hitResult2Infomations;

@Mixin(ServerPlayer.class)
public abstract class serverPlayerMixin extends Player {
    @Shadow
    public abstract void sendSystemMessage(@NotNull Component message);

    public serverPlayerMixin(Level world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Unique
    private int multiLoader_1_20_1$spyGlassHUDcooldown = 0;

    @Inject(method = "tick", at = @At("HEAD"))
    private void tickInject(CallbackInfo ci) {

        if (ModConfigs.spyglass_what_is_that == ModConfigs.inspectType.DISABLE)
            return;

        Item handItem = this.getMainHandItem().getItem();
        Item offHandItem = this.getOffhandItem().getItem();

        if (handItem == Items.SPYGLASS || offHandItem == Items.SPYGLASS) {
            if (multiLoader_1_20_1$spyGlassHUDcooldown != 20) {
                multiLoader_1_20_1$spyGlassHUDcooldown++;
                return;
            }
            multiLoader_1_20_1$spyGlassHUDcooldown = 0;

            int distance = this.isScoping() ? 32 : 5;

            HitResult hit = this.pick(distance, 0, false);

            //raycast for entity
            if (ModConfigs.spyglass_what_is_that == ModConfigs.inspectType.ALL
                    || ModConfigs.spyglass_what_is_that == ModConfigs.inspectType.ENTITY_ONLY
            ) {
                Vec3 min = this.getEyePosition(0);
                double sqrdDist = distance * distance;

                if (hit != null) {
                    sqrdDist = hit.getLocation().distanceToSqr(this.getEyePosition());
                }

                Vec3 vec3d2 = this.getViewVector(1.0F);
                Vec3 max = min.add(vec3d2.x * distance, vec3d2.y * distance, vec3d2.z * distance);

                AABB box = this.getBoundingBox().expandTowards(vec3d2.scale(distance)).inflate(1.0D, 1.0D, 1.0D);
                EntityHitResult hitResult = ProjectileUtil.getEntityHitResult(this, min, max, box, this::isTargettable_diversity, sqrdDist);

                if (hitResult != null)
                    hit = hitResult;
            }

            if (hit == null)
                return;

            //raycast result entity
            if (hit.getType() == HitResult.Type.ENTITY
                    && ((
                    ModConfigs.spyglass_what_is_that == ModConfigs.inspectType.ALL
                            || ModConfigs.spyglass_what_is_that == ModConfigs.inspectType.ENTITY_ONLY
            ))
            ) {
                EntityHitResult entityHitResult = (EntityHitResult) hit;
                this.displayClientMessage(hitResult2Infomations.parseLivingEntity(this.level(), this, entityHitResult.getEntity()), true);
            }
            //raycast block
            else if (hit.getType() == HitResult.Type.BLOCK
                    && ((
                    ModConfigs.spyglass_what_is_that == ModConfigs.inspectType.ALL
                            || ModConfigs.spyglass_what_is_that == ModConfigs.inspectType.BLOCK_ONLY
            ))
            ) {
                BlockHitResult blockHit = (BlockHitResult) hit;
                this.displayClientMessage(hitResult2Infomations.parseBlockState(this.level(), this, blockHit.getBlockPos()), true);
            }
        }
    }

    @Unique
    private boolean isTargettable_diversity(Entity entity) {
        return !entity.isSpectator() && entity.isPickable() && !entity.isInvisibleTo(this);
    }
}
