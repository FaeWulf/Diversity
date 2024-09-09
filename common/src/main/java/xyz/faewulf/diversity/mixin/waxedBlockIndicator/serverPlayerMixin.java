package xyz.faewulf.diversity.mixin.waxedBlockIndicator;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.faewulf.diversity.util.config.ModConfigs;

@Mixin(ServerPlayer.class)
public abstract class serverPlayerMixin extends Player {

    @Unique
    private int diversity_SpyglassUseTime = 0;

    @Shadow
    public abstract ServerLevel serverLevel();

    @Shadow
    public abstract void sendSystemMessage(Component message);

    @Shadow
    public abstract void dismountTo(double destX, double destY, double destZ);

    public serverPlayerMixin(Level world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void tickInject(CallbackInfo ci) {

        if (!ModConfigs.waxed_copper_indicator)
            return;

        Item handItem = this.getMainHandItem().getItem();
        Item offHandItem = this.getOffhandItem().getItem();

        if ((handItem == Items.SPYGLASS || offHandItem == Items.SPYGLASS) && this.isShiftKeyDown()) {

            if (diversity_SpyglassUseTime == 0) {
                BlockPos playerPos = this.blockPosition();
                final int radius = 5;
                BlockPos minPos = new BlockPos(playerPos.getX() - radius, playerPos.getY() - radius, playerPos.getZ() - radius);
                BlockPos maxPos = new BlockPos(playerPos.getX() + radius, playerPos.getY() + radius, playerPos.getZ() + radius);
                for (BlockPos pos : BlockPos.betweenClosed(minPos, maxPos)) {
                    Block currentBlock = this.serverLevel().getBlockState(pos).getBlock();

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

                            this.serverLevel().sendParticles(ParticleTypes.WAX_ON, x, y, z, 1, 0, 0, 0, 0);
                        }
                    }
                }
            }

            diversity_SpyglassUseTime = diversity_SpyglassUseTime > 20 ? 0 : diversity_SpyglassUseTime + 1;
        }

    }
}
