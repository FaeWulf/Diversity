package xyz.faewulf.diversity.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LevelEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.faewulf.diversity.inter.ICustomSniffer;
import xyz.faewulf.diversity.inter.typeSnort;

@Mixin(Sniffer.class)
public abstract class SnifferSniffStuff extends Animal implements ICustomSniffer {

    @Shadow
    public abstract Sniffer transitionTo(Sniffer.State pState);

    @Unique
    private typeSnort snortType;

    @Unique
    private int snortState = 0;

    @Unique
    private int snortTimer = 0;

    protected SnifferSniffStuff(EntityType<? extends Animal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(at = @At("HEAD"), method = "tick")
    private void tick(CallbackInfo ci) {
        if (this.snortType == null)
            return;

        if (this.level().isClientSide)
            return;

        if (snortTimer >= 30) {
            snortTimer = 0;
            snortState++;

            if (snortState % 2 == 0)
                this.transitionTo(Sniffer.State.SNIFFING);
            else
                this.transitionTo(Sniffer.State.SCENTING);

            this.playSound(SoundEvents.SNIFFER_IDLE, 1.0f, 1.0f);

            if (snortState > 5) {
                snortState = 0;
                if (this.level() instanceof ServerLevel serverWorld && snortType != null) {
                    switch (snortType) {
                        case GUN_POWDER, REDSTONE -> {
                            serverWorld.explode(null, this.getX(), this.getY(), this.getZ(), 2.0f, Level.ExplosionInteraction.NONE);
                        }

                        case BLAZE_POWDER, GLOW_DUST -> {
                            serverWorld.explode(null, this.getX(), this.getY(), this.getZ(), 1.0f, true, Level.ExplosionInteraction.NONE);
                        }

                        case SUGAR -> {
                            serverWorld.explode(null, this.getX(), this.getY(), this.getZ(), 0.0f, Level.ExplosionInteraction.NONE);
                        }

                        case BONE_MEAL -> {

                            ItemStack bonemeal = new ItemStack(Items.BONE_MEAL);

                            BlockPos bonemealPos = this.blockPosition().below();

                            BoneMealItem.growCrop(bonemeal, serverWorld, bonemealPos);
                            serverWorld.levelEvent(LevelEvent.PARTICLES_AND_SOUND_PLANT_GROWTH, bonemealPos, 15);
                            serverWorld.explode(null, this.getX(), this.getY(), this.getZ(), 0.5f, Level.ExplosionInteraction.NONE);
                        }
                    }
                }

                snortType = null;
            }
        }

        snortTimer++;
    }

    @Override
    public typeSnort getSnortType() {
        return snortType;
    }

    public void setSnortType(typeSnort snortType) {
        this.snortType = snortType;
    }
}