package xyz.faewulf.diversity.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import xyz.faewulf.diversity.feature.entity.pseudoBlockEntity.PseudoBlockEntity;

import java.util.List;

public class pseudoBlockEntityUtil {
    public static PseudoBlockEntity getBlockEntity(Level level, BlockPos blockPos) {
        AABB box = new AABB(
                blockPos.getX() + 0.3f, blockPos.getY() + 0.3f, blockPos.getZ() + 0.3f,
                blockPos.getX() + 0.7f, blockPos.getY() + 0.7f, blockPos.getZ() + 0.7f
        );

        List<Entity> entitiesWithinRadius = level.getEntitiesOfClass(Entity.class, box, entity -> entity.getType() == EntityType.TEXT_DISPLAY);

        if (entitiesWithinRadius.isEmpty())
            return null;

        return (PseudoBlockEntity) entitiesWithinRadius.getFirst();
    }
}
