package xyz.faewulf.diversity.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
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

import static net.minecraft.world.entity.projectile.windcharge.AbstractWindCharge.EXPLOSION_DAMAGE_CALCULATOR;

@Mixin(Sniffer.class)
public abstract class SnifferSniffStuff extends Animal implements ICustomSniffer {

    @Unique
    private typeSnort diversity_Multiloader$snortType;
    @Unique
    private int diversity_Multiloader$snortState = 0;
    @Unique
    private int diversity_Multiloader$snortTimer = 0;

    protected SnifferSniffStuff(EntityType<? extends Animal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Shadow
    public abstract Sniffer transitionTo(Sniffer.State pState);

    @Inject(at = @At("HEAD"), method = "tick")
    private void tick(CallbackInfo ci) {
        if (this.diversity_Multiloader$snortType == null)
            return;

        if (this.level().isClientSide)
            return;

        if (diversity_Multiloader$snortTimer >= 30) {
            diversity_Multiloader$snortTimer = 0;
            diversity_Multiloader$snortState++;

            if (diversity_Multiloader$snortState % 2 == 0)
                this.transitionTo(Sniffer.State.SNIFFING);
            else
                this.transitionTo(Sniffer.State.SCENTING);

            this.playSound(SoundEvents.SNIFFER_IDLE, 1.0f, 1.0f);

            if (diversity_Multiloader$snortState > 5) {
                diversity_Multiloader$snortState = 0;
                if (this.level() instanceof ServerLevel serverWorld && diversity_Multiloader$snortType != null) {
                    switch (diversity_Multiloader$snortType) {
                        case typeSnort.GUN_POWDER -> {
                            serverWorld.explode(null, this.getX(), this.getY(), this.getZ(), 2.0f, Level.ExplosionInteraction.NONE);
                        }

                        case typeSnort.BLAZE_POWDER, typeSnort.GLOW_DUST -> {
                            serverWorld.explode(null, this.getX(), this.getY(), this.getZ(), 1.0f, true, Level.ExplosionInteraction.NONE);
                        }

                        case typeSnort.SUGAR -> {
                            serverWorld.explode(null, this.getX(), this.getY(), this.getZ(), 0.0f, Level.ExplosionInteraction.NONE);
                        }

                        case typeSnort.BONE_MEAL -> {

                            ItemStack bonemeal = new ItemStack(Items.BONE_MEAL);

                            BlockPos bonemealPos = this.blockPosition().below();

                            BoneMealItem.growCrop(bonemeal, serverWorld, bonemealPos);
                            serverWorld.levelEvent(LevelEvent.PARTICLES_AND_SOUND_PLANT_GROWTH, bonemealPos, 15);
                            serverWorld.explode(null, this.getX(), this.getY(), this.getZ(), 0.5f, Level.ExplosionInteraction.NONE);
                        }

                        case typeSnort.REDSTONE -> {
                            serverWorld.explode(
                                    null,
                                    null,
                                    EXPLOSION_DAMAGE_CALCULATOR,
                                    this.getX(),
                                    this.getY(),
                                    this.getZ(),
                                    2.0F,
                                    false,
                                    Level.ExplosionInteraction.TRIGGER,
                                    ParticleTypes.GUST_EMITTER_SMALL,
                                    ParticleTypes.GUST_EMITTER_LARGE,
                                    SoundEvents.WIND_CHARGE_BURST
                            );
                        }
                    }


                }

                diversity_Multiloader$snortType = null;
            }
        }

        diversity_Multiloader$snortTimer++;
    }

    @Override
    public typeSnort diversity_Multiloader$getSnortType() {
        return diversity_Multiloader$snortType;
    }

    public void diversity_Multiloader$setSnortType(typeSnort snortType) {
        this.diversity_Multiloader$snortType = snortType;
    }
}