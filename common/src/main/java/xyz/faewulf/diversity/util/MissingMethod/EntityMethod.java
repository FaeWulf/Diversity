package xyz.faewulf.diversity.util.MissingMethod;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class EntityMethod {
    public static Direction getNearestViewDirection(Entity entity) {
        Vec3 vec3 = entity.getViewVector(1.0F);
        return Direction.getNearest(vec3.x, vec3.y, vec3.z);
    }
}
