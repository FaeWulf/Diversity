package xyz.faewulf.diversity.feature.entity.fox;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import xyz.faewulf.diversity.inter.entity.ICustomFoxEntity;
import xyz.faewulf.diversity.util.CustomLootTables;

public class BuryItemGoal extends MoveToBlockGoal {

    private final Fox fox;
    private BlockPos targetPos;
    private int timer = 0;
    private int type = 0;

    public BuryItemGoal(Fox fox, final double speed, final int range, final int maxYDifference) {
        super(fox, speed, range, maxYDifference);
        this.fox = fox;
    }

    @Override
    public boolean canUse() {
        return !this.fox.isSleeping() && super.canUse() && !this.fox.isBaby() && ((ICustomFoxEntity) this.fox).getBuryCoolDown() <= 0;
    }

    @Override
    public void start() {
        this.timer = 0;
        this.type = 0;
        fox.setSitting(false);

        Block targetBlock = this.fox.level().getBlockState(this.blockPos.below()).getBlock();

        if (targetBlock == Blocks.SAND)
            type = 1;

        if (targetBlock == Blocks.GRAVEL)
            type = 2;


        super.start();
    }

    @Override
    public double acceptedDistance() {
        return 2.0;
    }

    @Override
    public void tick() {
        if (this.isReachedTarget()) {
            if (this.timer >= 20 * 4) {
                //after done
                BlockState susBlock = type == 1 ? Blocks.SUSPICIOUS_SAND.defaultBlockState() : Blocks.SUSPICIOUS_GRAVEL.defaultBlockState();

                this.fox.level().setBlockAndUpdate(this.blockPos.below(), susBlock);
                this.fox.level().gameEvent(this.fox, GameEvent.BLOCK_CHANGE, this.blockPos.below());

                this.fox.level().getBlockEntity(blockPos.below(), BlockEntityType.BRUSHABLE_BLOCK).ifPresent(
                        blockEntity -> blockEntity.setLootTable(CustomLootTables.FOX_BURY, this.fox.getRandom().nextLong())
                );

                if (type == 1)
                    this.fox.playSound(SoundEvents.SAND_BREAK, 1.0F, 1.0F);
                else
                    this.fox.playSound(SoundEvents.GRAVEL_BREAK, 1.0F, 1.0F);

                //set cooldown
                ((ICustomFoxEntity) this.fox).setBuryCoolDown(24000);
            } else {
                //particle
                if (this.timer % 10 == 0)
                    if (this.fox.level() instanceof ServerLevel serverWorld) {

                        BlockParticleOption blockStateParticleEffect = new BlockParticleOption(ParticleTypes.BLOCK, type == 1 ? Blocks.SAND.defaultBlockState() : Blocks.GRAVEL.defaultBlockState());
                        Vec3 particlePos = this.blockPos.getCenter();
                        serverWorld.sendParticles(blockStateParticleEffect, particlePos.x, particlePos.y - 0.4, particlePos.z, 10, 0.3, 0.1, 0.3, 1);

                        if (type == 1)
                            this.fox.playSound(SoundEvents.SAND_HIT, 1.0F, 1.0F);
                        else
                            this.fox.playSound(SoundEvents.GRAVEL_HIT, 1.0F, 1.0F);
                    }

                ++this.timer;
            }
        } else if (!this.isReachedTarget() && this.fox.getRandom().nextFloat() < 0.05F) {
            fox.playSound(SoundEvents.FOX_SNIFF, 1.0F, 1.0F);
        }

        super.tick();
    }

    @Override
    public boolean shouldRecalculatePath() {
        return this.tryTicks % 100 == 0;
    }


    @Override
    protected boolean isValidTarget(LevelReader world, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos);
        BlockState blockStateBelow = world.getBlockState(pos.below());

        if (blockState.isAir() && (blockStateBelow.is(Blocks.SAND) || blockStateBelow.is(Blocks.GRAVEL))) {
            this.blockPos = pos;
            return true;
        }

        return false;
    }
}

