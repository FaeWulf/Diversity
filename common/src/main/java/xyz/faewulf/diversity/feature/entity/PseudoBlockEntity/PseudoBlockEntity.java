package xyz.faewulf.diversity.feature.entity.PseudoBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public interface PseudoBlockEntity {
    public static PseudoBlockEntity getBlockEntity(Level level, BlockPos blockPos) {
        return null;
    }

    String getEntityType();

    void setEntityType(String value);

    /**
     * Checks if a block entity of type {@link EntityType#TEXT_DISPLAY} already exists within a small
     * radius around the current block position.
     *
     * <p>This method creates an axis-aligned bounding box (AABB) centered at the block position with
     * an offset of 0.3 to 0.7 units in all directions. It then checks the level for any entities of type
     * {@link EntityType#TEXT_DISPLAY} within that box. If any such entities are found, the method
     * returns {@code true}, indicating that a block entity already exists in the vicinity.</p>
     *
     * @return {@code true} if a block entity of type {@link EntityType#TEXT_DISPLAY} is found
     * within the specified area, {@code false} otherwise
     */
    boolean isBlockEntityAlreadyExist();

    void setBlockTickFunction(Consumer<Display> callback);

    /**
     * Determines the condition under which the current entity should be discarded.
     *
     * <p>This method is abstract and must be implemented by any subclass to define
     * the specific condition that will trigger the discard action. The method should
     * return {@code true} if the object meets the criteria for discarding, and {@code false}
     * otherwise.</p>
     *
     * @return {@code true} if this entity should be discarded, {@code false} otherwise
     */
    void setDiscardWhenFunction(Function<Display, Boolean> callback);

    void setParentBlockType(Block... block);

    void setDelayTick(int value);

    List<String> getParentTag();

    void setParentTag(String... tag);
}