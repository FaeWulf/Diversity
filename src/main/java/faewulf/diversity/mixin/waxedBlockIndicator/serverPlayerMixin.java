package faewulf.diversity.mixin.waxedBlockIndicator;

import com.mojang.authlib.GameProfile;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class serverPlayerMixin extends PlayerEntity {

    @Unique
    private int diversity_SpyglassUseTime = 0;

    @Shadow
    public abstract ServerWorld getServerWorld();

    @Shadow
    public abstract void sendMessage(Text message);

    @Shadow
    public abstract void playerTick();

    @Shadow
    public abstract void requestTeleportAndDismount(double destX, double destY, double destZ);

    public serverPlayerMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void tickInject(CallbackInfo ci) {


        Item handItem = this.getMainHandStack().getItem();
        Item offHandItem = this.getOffHandStack().getItem();

        if ((handItem == Items.SPYGLASS || offHandItem == Items.SPYGLASS) && this.isSneaking()) {

            if (diversity_SpyglassUseTime == 0) {
                BlockPos playerPos = this.getBlockPos();
                final int radius = 5;
                BlockPos minPos = new BlockPos(playerPos.getX() - radius, playerPos.getY() - radius, playerPos.getZ() - radius);
                BlockPos maxPos = new BlockPos(playerPos.getX() + radius, playerPos.getY() + radius, playerPos.getZ() + radius);
                for (BlockPos pos : BlockPos.iterate(minPos, maxPos)) {
                    Block currentBlock = this.getServerWorld().getBlockState(pos).getBlock();

                    if (currentBlock.getName().getString().toLowerCase().contains("waxed")) {
                        for (int i = 0; i < 10; i++) {
                            double x = pos.getX() + 0.5;
                            double y = pos.getY() + 0.5;
                            double z = pos.getZ() + 0.5;

                            Direction face = Direction.values()[this.random.nextInt(Direction.values().length)];
                            //for particle to spawn in surface only
                            switch (face) {
                                case UP:
                                    y = pos.getY() + 1.05;
                                    x += this.random.nextDouble() - 0.55;
                                    z += this.random.nextDouble() - 0.55;
                                    break;
                                case DOWN:
                                    y = pos.getY() - 0.05;
                                    x += this.random.nextDouble() - 0.55;
                                    z += this.random.nextDouble() - 0.55;
                                    break;
                                case NORTH:
                                    z = pos.getZ() - 0.05;
                                    x += random.nextDouble() - 0.55;
                                    y += random.nextDouble() - 0.55;
                                    break;
                                case SOUTH:
                                    z = pos.getZ() + 1.05;
                                    x += this.random.nextDouble() - 0.55;
                                    y += this.random.nextDouble() - 0.55;
                                    break;
                                case WEST:
                                    x = pos.getX() - 0.05;
                                    y += this.random.nextDouble() - 0.55;
                                    z += this.random.nextDouble() - 0.55;
                                    break;
                                case EAST:
                                    x = pos.getX() + 1.05;
                                    y += this.random.nextDouble() - 0.55;
                                    z += this.random.nextDouble() - 0.55;
                                    break;
                            }

                            this.getServerWorld().spawnParticles(ParticleTypes.WAX_ON, x, y, z, 1, 0, 0, 0, 0);

                        }
                    }
                }
            }

            diversity_SpyglassUseTime = diversity_SpyglassUseTime > 20 ? 0 : diversity_SpyglassUseTime + 1;
        }

    }
}
