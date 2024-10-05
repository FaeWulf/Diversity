package xyz.faewulf.diversity.mixin.slimeChunkHint;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.faewulf.diversity.util.compare;
import xyz.faewulf.diversity.util.config.ModConfigs;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow
    @Final
    protected RandomSource random;

    @Inject(method = "playStepSound", at = @At("TAIL"))
    private void playStepSoundMixin(BlockPos pPos, BlockState pState, CallbackInfo ci) {

        if (!ModConfigs.slime_chunk_check)
            return;

        if ((Object) this instanceof ServerPlayer serverPlayer) {

            //trigger
            //if not sneaking, the chance is 0.1
            //if sneaking then the chance is 0.7
            if ((!serverPlayer.isShiftKeyDown() && this.random.nextFloat() > 0.1f)
                    || (serverPlayer.isShiftKeyDown() && this.random.nextFloat() < 0.3f)
            ) {
                return;
            }

            boolean holdingSlimeBlock = false;

            //for each hand
            for (ItemStack itemStack : serverPlayer.getHandSlots()) {
                //if has tag
                if (compare.isHasTag(itemStack.getItem(), "diversity:slime_detector"))
                    holdingSlimeBlock = true;
            }

            if (serverPlayer.level() instanceof ServerLevel serverLevel && holdingSlimeBlock) {

                ChunkPos chunkPos = new ChunkPos(serverPlayer.blockPosition());
                long seed = ((WorldGenLevel) serverLevel).getSeed();

                //check slimechunk
                boolean isSlimeChunk = WorldgenRandom.seedSlimeChunk(chunkPos.x, chunkPos.z, seed, 987234911L).nextInt(10) == 0;

                if (isSlimeChunk) {
                    serverLevel.sendParticles(
                            ParticleTypes.ITEM_SLIME,
                            serverPlayer.getX(),
                            serverPlayer.getY(),
                            serverPlayer.getZ(),
                            10, 0.3f, 0.06d, 0.3f, 0.0d);

                    serverLevel.playSound(null,
                            serverPlayer.getX(),
                            serverPlayer.getY(),
                            serverPlayer.getZ(),
                            SoundEvents.SLIME_SQUISH,
                            SoundSource.PLAYERS,
                            0.2f,
                            0.1f
                    );
                }
            }
        }
    }
}
