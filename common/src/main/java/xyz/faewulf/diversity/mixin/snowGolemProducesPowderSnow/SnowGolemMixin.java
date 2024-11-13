package xyz.faewulf.diversity.mixin.snowGolemProducesPowderSnow;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Shearable;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.faewulf.diversity.util.config.ModConfigs;

@Mixin(SnowGolem.class)
public abstract class SnowGolemMixin extends AbstractGolem implements Shearable, RangedAttackMob {
    @Unique
    private int diversity$coolDown = 0;

    protected SnowGolemMixin(EntityType<? extends AbstractGolem> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;defaultBlockState()Lnet/minecraft/world/level/block/state/BlockState;"))
    private void aiStepInject(CallbackInfo ci) {
        if (this.level() instanceof ServerLevel serverLevel && ModConfigs.snow_golem_produces_powder_snow && diversity$coolDown <= 0) {
            diversity$coolDown = 600;
            if (this.random.nextInt(15) == 0) {

                BlockState blockState = serverLevel.getBlockState(this.blockPosition());

                if (blockState.getBlock() == Blocks.CAULDRON) {
                    serverLevel.setBlockAndUpdate(this.blockPosition(), Blocks.POWDER_SNOW_CAULDRON.defaultBlockState());
                    serverLevel.playSound(this, this.blockPosition(), SoundEvents.POWDER_SNOW_BREAK, SoundSource.BLOCKS, 1.0f, 1.0f);
                    serverLevel.gameEvent(this, GameEvent.ENTITY_PLACE, this.blockPosition());
                }

                if (blockState.getBlock() == Blocks.POWDER_SNOW_CAULDRON) {
                    BlockState cycle = blockState.cycle(LayeredCauldronBlock.LEVEL);
                    serverLevel.setBlockAndUpdate(this.blockPosition(), cycle);
                    serverLevel.playSound(this, this.blockPosition(), SoundEvents.POWDER_SNOW_BREAK, SoundSource.BLOCKS, 1.0f, 1.0f);
                    serverLevel.gameEvent(this, GameEvent.ENTITY_PLACE, this.blockPosition());
                }
            }
        }

        diversity$coolDown--;
    }
}
