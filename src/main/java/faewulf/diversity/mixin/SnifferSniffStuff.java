package faewulf.diversity.mixin;

import faewulf.diversity.inter.ICustomSniffer;
import faewulf.diversity.inter.typeSnort;
import net.minecraft.entity.passive.SnifferEntity;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.entity.projectile.AbstractWindChargeEntity.EXPLOSION_BEHAVIOR;

@Mixin(SnifferEntity.class)
public class SnifferSniffStuff implements ICustomSniffer {

    @Unique
    private typeSnort snortType;

    @Unique
    private int snortState = 0;

    @Unique
    private int snortTimer = 0;

    @Inject(at = @At("HEAD"), method = "tick")
    private void tick(CallbackInfo ci) {
        if (this.snortType == null)
            return;

        SnifferEntity _this = ((SnifferEntity) (Object) this);

        if (_this.getWorld().isClient)
            return;

        if (snortTimer >= 30) {
            snortTimer = 0;
            snortState++;

            if (snortState % 2 == 0)
                _this.startState(SnifferEntity.State.SNIFFING);
            else
                _this.startState(SnifferEntity.State.SCENTING);

            _this.playSound(SoundEvents.ENTITY_SNIFFER_IDLE, 1.0f, 1.0f);

            if (snortState > 5) {
                snortState = 0;
                if (_this.getWorld() instanceof ServerWorld serverWorld && snortType != null) {
                    switch (snortType) {
                        case typeSnort.GUN_POWDER -> {
                            serverWorld.createExplosion(null, _this.getX(), _this.getY(), _this.getZ(), 2.0f, World.ExplosionSourceType.NONE);
                        }

                        case typeSnort.BLAZE_POWDER, typeSnort.GLOW_DUST -> {
                            serverWorld.createExplosion(null, _this.getX(), _this.getY(), _this.getZ(), 1.0f, true, World.ExplosionSourceType.NONE);
                        }

                        case typeSnort.SUGAR -> {
                            serverWorld.createExplosion(null, _this.getX(), _this.getY(), _this.getZ(), 0.0f, World.ExplosionSourceType.NONE);
                        }

                        case typeSnort.BONE_MEAL -> {

                            ItemStack bonemeal = new ItemStack(Items.BONE_MEAL);

                            BlockPos bonemealPos = _this.getBlockPos().down();

                            BoneMealItem.useOnFertilizable(bonemeal, serverWorld, bonemealPos);
                            serverWorld.syncWorldEvent(WorldEvents.BONE_MEAL_USED, bonemealPos, 15);
                            serverWorld.createExplosion(null, _this.getX(), _this.getY(), _this.getZ(), 0.5f, World.ExplosionSourceType.NONE);
                        }

                        case typeSnort.REDSTONE -> {
                            serverWorld.createExplosion(
                                    null,
                                    null,
                                    EXPLOSION_BEHAVIOR,
                                    _this.getX(),
                                    _this.getY(),
                                    _this.getZ(),
                                    2.0F,
                                    false,
                                    World.ExplosionSourceType.TRIGGER,
                                    ParticleTypes.GUST_EMITTER_SMALL,
                                    ParticleTypes.GUST_EMITTER_LARGE,
                                    SoundEvents.ENTITY_WIND_CHARGE_WIND_BURST
                            );
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