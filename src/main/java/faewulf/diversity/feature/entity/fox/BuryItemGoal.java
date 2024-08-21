package faewulf.diversity.feature.entity.fox;

import faewulf.diversity.inter.entity.ICustomFoxEntity;
import faewulf.diversity.util.CustomLootTables;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ai.goal.MoveToTargetPosGoal;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldView;
import net.minecraft.world.event.GameEvent;

public class BuryItemGoal extends MoveToTargetPosGoal {

    private final FoxEntity fox;
    private BlockPos targetPos;
    private int timer = 0;
    private int type = 0;

    public BuryItemGoal(FoxEntity fox, final double speed, final int range, final int maxYDifference) {
        super(fox, speed, range, maxYDifference);
        this.fox = fox;
    }

    @Override
    public boolean canStart() {
        return !this.fox.isSleeping() && super.canStart() && !this.fox.isBaby() && ((ICustomFoxEntity) this.fox).getBuryCoolDown() <= 0;
    }

    @Override
    public void start() {
        this.timer = 0;
        this.type = 0;
        fox.setSitting(false);

        Block targetBlock = this.fox.getWorld().getBlockState(this.targetPos.down()).getBlock();

        if (targetBlock == Blocks.SAND)
            type = 1;

        if (targetBlock == Blocks.GRAVEL)
            type = 2;


        super.start();
    }

    @Override
    public double getDesiredDistanceToTarget() {
        return 2.0;
    }

    @Override
    public void tick() {
        if (this.hasReached()) {
            if (this.timer >= 20 * 4) {
                //after done
                BlockState susBlock = type == 1 ? Blocks.SUSPICIOUS_SAND.getDefaultState() : Blocks.SUSPICIOUS_GRAVEL.getDefaultState();

                this.fox.getWorld().setBlockState(this.targetPos.down(), susBlock);
                this.fox.getWorld().emitGameEvent(this.fox, GameEvent.BLOCK_CHANGE, this.targetPos.down());

                //? >=1.21 {

                /*this.fox.getWorld().getBlockEntity(targetPos.down(), BlockEntityType.BRUSHABLE_BLOCK).ifPresent(
                        blockEntity -> blockEntity.setLootTable(CustomLootTables.FOX_BURY, this.fox.getRandom().nextLong())
                );

                *///?}

                //? =1.20.1 {
                this.fox.getWorld().getBlockEntity(targetPos.down(), BlockEntityType.BRUSHABLE_BLOCK).ifPresent(
                        blockEntity -> blockEntity.setLootTable(Identifier.of(Identifier.DEFAULT_NAMESPACE, "entity/fox_bury_behavior"), this.fox.getRandom().nextLong())
                );
                //?}


                if (type == 1)
                    this.fox.playSound(SoundEvents.BLOCK_SAND_BREAK, 1.0F, 1.0F);
                else
                    this.fox.playSound(SoundEvents.BLOCK_GRAVEL_BREAK, 1.0F, 1.0F);

                //set cooldown
                ((ICustomFoxEntity) this.fox).setBuryCoolDown(24000);
            } else {
                //particle
                if (this.timer % 10 == 0)
                    if (this.fox.getWorld() instanceof ServerWorld serverWorld) {

                        BlockStateParticleEffect blockStateParticleEffect = new BlockStateParticleEffect(ParticleTypes.BLOCK, type == 1 ? Blocks.SAND.getDefaultState() : Blocks.GRAVEL.getDefaultState());
                        Vec3d particlePos = this.targetPos.toCenterPos();
                        serverWorld.spawnParticles(blockStateParticleEffect, particlePos.x, particlePos.y - 0.4, particlePos.z, 10, 0.3, 0.1, 0.3, 1);

                        if (type == 1)
                            this.fox.playSound(SoundEvents.BLOCK_SAND_HIT, 1.0F, 1.0F);
                        else
                            this.fox.playSound(SoundEvents.BLOCK_GRAVEL_HIT, 1.0F, 1.0F);
                    }

                ++this.timer;
            }
        } else if (!this.hasReached() && this.fox.getRandom().nextFloat() < 0.05F) {
            fox.playSound(SoundEvents.ENTITY_FOX_SNIFF, 1.0F, 1.0F);
        }

        super.tick();
    }

    @Override
    public boolean shouldResetPath() {
        return this.tryingTime % 100 == 0;
    }


    @Override
    protected boolean isTargetPos(WorldView world, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos);
        BlockState blockStateBelow = world.getBlockState(pos.down());

        if (blockState.isAir() && (blockStateBelow.isOf(Blocks.SAND) || blockStateBelow.isOf(Blocks.GRAVEL))) {
            this.targetPos = pos;
            return true;
        }

        return false;
    }
}

