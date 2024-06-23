package faewulf.diversity.mixin.spyglassWhatIsThat;

import com.mojang.authlib.GameProfile;
import faewulf.diversity.util.ModConfigs;
import faewulf.diversity.util.hitResult2Infomations;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class serverPlayerMixin extends PlayerEntity {
    @Shadow
    public abstract void sendMessage(Text message);

    @Shadow
    public abstract ServerWorld getServerWorld();

    @Shadow
    protected abstract void worldChanged(ServerWorld origin);

    public serverPlayerMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Unique
    private int spyGlassHUDcooldown = 0;

    @Inject(method = "tick", at = @At("HEAD"))
    private void tickInject(CallbackInfo ci) {

        if (ModConfigs.spyglass_what_is_that == ModConfigs.inspectType.DISABLE)
            return;

        Item handItem = this.getMainHandStack().getItem();
        Item offHandItem = this.getOffHandStack().getItem();

        if (handItem == Items.SPYGLASS || offHandItem == Items.SPYGLASS) {
            if (spyGlassHUDcooldown != 20) {
                spyGlassHUDcooldown++;
                return;
            }
            spyGlassHUDcooldown = 0;

            int distance = this.isUsingSpyglass() ? 32 : 5;

            HitResult hit = this.raycast(distance, 0, false);

            //raycast for entity
            if (ModConfigs.spyglass_what_is_that == ModConfigs.inspectType.ALL
                    || ModConfigs.spyglass_what_is_that == ModConfigs.inspectType.ENTITY_ONLY
            ) {
                Vec3d min = this.getCameraPosVec(0);
                double sqrdDist = distance * distance;

                if (hit != null) {
                    sqrdDist = hit.getPos().squaredDistanceTo(this.getEyePos());
                }

                Vec3d vec3d2 = this.getRotationVec(1.0F);
                Vec3d max = min.add(vec3d2.x * distance, vec3d2.y * distance, vec3d2.z * distance);

                Box box = this.getBoundingBox().stretch(vec3d2.multiply(distance)).expand(1.0D, 1.0D, 1.0D);
                EntityHitResult hitResult = ProjectileUtil.raycast(this, min, max, box, this::isTargettable_diversity, sqrdDist);

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
                this.sendMessage(hitResult2Infomations.parseLivingEntity(this.getWorld(), this, entityHitResult.getEntity()), true);
            }
            //raycast block
            else if (hit.getType() == HitResult.Type.BLOCK
                    && ((
                    ModConfigs.spyglass_what_is_that == ModConfigs.inspectType.ALL
                            || ModConfigs.spyglass_what_is_that == ModConfigs.inspectType.BLOCK_ONLY
            ))
            ) {
                BlockHitResult blockHit = (BlockHitResult) hit;
                this.sendMessage(hitResult2Infomations.parseBlockState(this.getWorld(), this, blockHit.getBlockPos()), true);
            }
        }
    }

    @Unique
    private boolean isTargettable_diversity(Entity entity) {
        return !entity.isSpectator() && entity.canHit() && !entity.isInvisibleTo(this);
    }
}
