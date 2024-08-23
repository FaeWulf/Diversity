package xyz.faewulf.diversity.mixin.wetSponeBlockDryInWarmBiome;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.faewulf.diversity.inter.ICustomPseudoEntityBlock;

import java.util.Objects;


@Mixin(Display.class)
public abstract class TextDisplayMixin extends Entity implements ICustomPseudoEntityBlock {
    public TextDisplayMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Unique
    private String diversity_pseudoMode;

    @Unique
    private int diversity_tickDelay = 0;

    @Inject(method = "tick", at = @At("HEAD"))
    private void tickInject(CallbackInfo ci) {

        if (this.level().isClientSide)
            return;

        if (this.diversity_pseudoMode == null) {
            //this.discard();
            return;
        }

        if (!Objects.equals(diversity_pseudoMode, "wetSponge"))
            return;

        if (this.level().getBlockState(this.blockPosition()).getBlock() != Blocks.WET_SPONGE) {
            this.discard();
            return;
        }

        //for tick every 1sec
        if (diversity_tickDelay < 20) {
            diversity_tickDelay++;
            return;
        }
        diversity_tickDelay = 0;

        ServerLevel world = (ServerLevel) this.level();
        BlockPos pos = this.blockPosition();

        //if warm biome
        if (world.getBiome(pos).value().getBaseTemperature() > 1.0f)
            if (this.random.nextInt(20) == 2) {
                world.setBlock(pos, Blocks.SPONGE.defaultBlockState(), Block.UPDATE_ALL);
                world.levelEvent(LevelEvent.PARTICLES_WATER_EVAPORATING, pos, 0);
                world.playSound(null, pos, SoundEvents.WET_SPONGE_DRIES, SoundSource.BLOCKS, 1.0F, (1.0F + world.getRandom().nextFloat() * 0.2F) * 0.7F);
                this.discard();
            } else {
                Vec3 center = pos.getCenter();
                world.sendParticles(ParticleTypes.CLOUD, center.x, center.y + 0.6, center.z, 3, 0.3, 0, 0.3, 0);
            }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void addAdditionalSaveData(CompoundTag nbt, CallbackInfo ci) {
        if (this.diversity_pseudoMode != null)
            nbt.putString("diversity:pseudoMode", this.diversity_pseudoMode);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readAdditionalSaveData(CompoundTag nbt, CallbackInfo ci) {
        if (nbt.contains("diversity:pseudoMode", Tag.TAG_STRING)) {
            this.diversity_pseudoMode = nbt.getString("diversity:pseudoMode");
        }
    }

    @Override
    public String getMode() {
        return diversity_pseudoMode;
    }

    @Override
    public void setMode(String value) {
        this.diversity_pseudoMode = value;
    }
}
