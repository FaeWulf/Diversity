package faewulf.diversity.mixin.wetSponeBlockDryInWarmBiome;

import faewulf.diversity.inter.ICustomPseudoEntityBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;


@Mixin(DisplayEntity.class)
public abstract class TextDisplayMixin extends Entity implements ICustomPseudoEntityBlock {
    public TextDisplayMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Unique
    private String diversity_pseudoMode;

    @Unique
    private int diversity_tickDelay = 0;

    @Inject(method = "tick", at = @At("HEAD"))
    private void tickInject(CallbackInfo ci) {

        if (this.getWorld().isClient)
            return;

        if (this.diversity_pseudoMode == null) {
            //this.discard();
            return;
        }

        if (!Objects.equals(diversity_pseudoMode, "wetSponge"))
            return;

        if (this.getWorld().getBlockState(this.getBlockPos()).getBlock() != Blocks.WET_SPONGE) {
            this.discard();
            return;
        }

        //for tick every 1sec
        if (diversity_tickDelay < 20) {
            diversity_tickDelay++;
            return;
        }
        diversity_tickDelay = 0;

        ServerWorld world = (ServerWorld) this.getWorld();
        BlockPos pos = this.getBlockPos();

        //if warm biome
        if (world.getBiome(pos).value().getTemperature() > 1.0f)
            if (this.random.nextInt(20) == 2) {
                world.setBlockState(pos, Blocks.SPONGE.getDefaultState(), Block.NOTIFY_ALL);
                world.syncWorldEvent(WorldEvents.WET_SPONGE_DRIES_OUT, pos, 0);

                //? if >=1.21 {
                /*world.playSound(null, pos, SoundEvents.BLOCK_WET_SPONGE_DRIES, SoundCategory.BLOCKS, 1.0F, (1.0F + world.getRandom().nextFloat() * 0.2F) * 0.7F);
                 *///?}

                //? if =1.20.1 {
                world.playSound(null, pos, SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.BLOCKS, 1.0F, (1.0F + world.getRandom().nextFloat() * 0.2F) * 0.7F);
                //?}

                this.discard();
            } else {
                Vec3d center = pos.toCenterPos();
                world.spawnParticles(ParticleTypes.CLOUD, center.x, center.y + 0.6, center.z, 3, 0.3, 0, 0.3, 0);
            }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        if (this.diversity_pseudoMode != null)
            nbt.putString("diversity:pseudoMode", this.diversity_pseudoMode);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("diversity:pseudoMode", NbtElement.STRING_TYPE)) {
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
