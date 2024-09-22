package xyz.faewulf.diversity.feature.entity.PseudoBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;

public class PseudoBlockEntities {
    public static Map<String, PseudoBlockEntityBuilder> PseudoBlockEntityList = new HashMap<>();

    public static PseudoBlockEntityBuilder STOP_GROW = register(
            "stop_grow",
            new PseudoBlockEntityBuilder()
                    .setParent(Blocks.SUGAR_CANE, Blocks.BAMBOO_SAPLING, Blocks.BAMBOO)
                    .setParentTag("minecraft:saplings", "diversity:trimmable")
                    .setDiscardWhenFunction(t -> {
                        BlockState blockState = t.level().getBlockState(t.blockPosition().above());
                        return blockState.getBlock() != Blocks.AIR;
                    })
    );

    public static PseudoBlockEntityBuilder WET_SPONGE = register(
            "wet_sponge",
            new PseudoBlockEntityBuilder()
                    .setParent(Blocks.WET_SPONGE)
                    .setTickDelay(10)
                    .setBlockTickFunction(t -> {
                        ServerLevel world = (ServerLevel) t.level();
                        BlockPos pos = t.blockPosition();

                        //if warm biome
                        if (world.getBiome(pos).value().getBaseTemperature() > 1.0f) {
                            if (world.random.nextInt(20) == 2) {
                                world.setBlock(pos, Blocks.SPONGE.defaultBlockState(), Block.UPDATE_ALL);
                                world.levelEvent(LevelEvent.PARTICLES_WATER_EVAPORATING, pos, 0);
                                world.playSound(null, pos, SoundEvents.WET_SPONGE_DRIES, SoundSource.BLOCKS, 1.0F, (1.0F + world.getRandom().nextFloat() * 0.2F) * 0.7F);

                                t.discard();
                            } else {
                                Vec3 center = pos.getCenter();
                                world.sendParticles(ParticleTypes.CLOUD, center.x, center.y + 0.6, center.z, 3, 0.3, 0, 0.3, 0);
                            }
                        } else
                            t.discard();
                    })
    );

    public static PseudoBlockEntityBuilder register(String name, PseudoBlockEntityBuilder pseudoBlockEntity) {
        PseudoBlockEntityList.put(name, pseudoBlockEntity.setBlockEntityType(name));
        return pseudoBlockEntity;
    }
}