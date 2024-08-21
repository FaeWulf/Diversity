package faewulf.diversity.mixin.invisibleItemFrame;

import faewulf.diversity.inter.ICustomItemFrame;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.AbstractDecorationEntity;

//? if >=1.21 {
/*import net.minecraft.entity.decoration.BlockAttachedEntity;*/
//?}

import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? if =1.20.1 {
@Mixin(AbstractDecorationEntity.class)
//?}

//? if >=1.21 {
/*@Mixin(BlockAttachedEntity.class)
 *///?}
public abstract class InvisibleItemFrameIndicator extends Entity {

    public InvisibleItemFrameIndicator(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo ci) {

        if (this.getWorld().isClient)
            return;

        if (this.getType() == EntityType.ITEM_FRAME || this.getType() == EntityType.GLOW_ITEM_FRAME) {

            //if does't have the require tag
            if (!((ICustomItemFrame) this).getIsInvisible())
                return;

            //? if >=1.21 {
            /*BlockAttachedEntity abstractDecorationEntity = ((BlockAttachedEntity) (Object) this);
             *///?}

            //? if =1.20.1 {
            AbstractDecorationEntity abstractDecorationEntity = ((AbstractDecorationEntity) (Object) this);
            //?}

            //if it is holding item the return
            if (!((ItemFrameEntity) abstractDecorationEntity).getHeldItemStack().isEmpty())
                return;

            Random random = Random.create();

            //? if >=1.21 {
            /*Direction direction = ((ItemFrameEntity) abstractDecorationEntity).getFacing();
             *///?}

            //? if =1.20.1 {
            Direction direction = abstractDecorationEntity.getHorizontalFacing();
            //?}

            BlockPos pos = this.getBlockPos();

            //each direction follow this formula: <position base value> - <direction value / 2> + 0.5 (center value) + <shift value>

            //<base value>: XYZ of the block
            //<direction value> (3 values: -1 0 1): to shift position based on the facing of itemFrame
            //<center value>: because <base value> isn't value from the center of the block.
            //<shift value> to make the particle stay in front of the itemFrame, if not add this value then particle plays inside and player couldnnt see it

            double d = (double) (pos.getX() - ((double) direction.getOffsetX() / 2)) + 0.5 + 0.1 * direction.getOffsetX();
            double e = (double) (pos.getY() - ((double) direction.getOffsetY() / 2)) + 0.5 + 0.1 * direction.getOffsetY();
            double f = (double) (pos.getZ() - ((double) direction.getOffsetZ() / 2)) + 0.5 + 0.1 * direction.getOffsetZ();
            double g = (double) (0.25F - (random.nextFloat()) * 0.5F);

            if (direction.getOffsetX() == 0) {
                d += random.nextInt(2) == 0 ? g : -g;
            }

            if (direction.getOffsetY() == 0) {
                e += random.nextInt(2) == 0 ? g : -g;
            }

            if (direction.getOffsetZ() == 0) {
                f += random.nextInt(2) == 0 ? g : -g;
            }


            if (random.nextInt(40) == 10) {
                ((ServerWorld) this.getWorld()).spawnParticles(
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
