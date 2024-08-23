package xyz.faewulf.diversity.mixin.invisibleItemFrame;

import xyz.faewulf.diversity.inter.ICustomItemFrame;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.BlockAttachedEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockAttachedEntity.class)
public class InvisibleItemFrameIndicator {

    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo ci) {
        BlockAttachedEntity _this = ((BlockAttachedEntity) (Object) this);

        if (_this.getType() == EntityType.ITEM_FRAME || _this.getType() == EntityType.GLOW_ITEM_FRAME) {

            if (_this.level().isClientSide)
                return;

            //if does't have the require tag
            if (!((ICustomItemFrame) _this).getIsInvisible())
                return;

            //if it is holding item the return
            if (!((ItemFrame) _this).getItem().isEmpty())
                return;

            RandomSource random = RandomSource.create();
            Direction direction = ((ItemFrame) _this).getNearestViewDirection();
            BlockPos pos = _this.blockPosition();

            //each direction follow this formula: <position base value> - <direction value / 2> + 0.5 (center value) + <shift value>

            //<base value>: XYZ of the block
            //<direction value> (3 values: -1 0 1): to shift position based on the facing of itemFrame
            //<center value>: because <base value> isn't value from the center of the block.
            //<shift value> to make the particle stay in front of the itemFrame, if not add this value then particle plays inside and player couldnnt see it

            double d = (double) (pos.getX() - ((double) direction.getStepX() / 2)) + 0.5 + 0.1 * direction.getStepX();
            double e = (double) (pos.getY() - ((double) direction.getStepY() / 2)) + 0.5 + 0.1 * direction.getStepY();
            double f = (double) (pos.getZ() - ((double) direction.getStepZ() / 2)) + 0.5 + 0.1 * direction.getStepZ();
            double g = (double) (0.25F - (random.nextFloat()) * 0.5F);

            if (direction.getStepX() == 0) {
                d += random.nextInt(2) == 0 ? g : -g;
            }

            if (direction.getStepY() == 0) {
                e += random.nextInt(2) == 0 ? g : -g;
            }

            if (direction.getStepZ() == 0) {
                f += random.nextInt(2) == 0 ? g : -g;
            }


            if (random.nextInt(40) == 10) {
                ((ServerLevel) _this.level()).sendParticles(
                        ParticleTypes.WAX_OFF,
                        d,
                        e,
                        f,
                        1,
                        random.nextGaussian() * 0.005,
                        random.nextGaussian() * 0.005,
                        random.nextGaussian() * 0.005,
                        0.001
                );
            }


        }
    }
}
