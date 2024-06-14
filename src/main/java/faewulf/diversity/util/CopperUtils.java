package faewulf.diversity.util;

import net.minecraft.block.BlockState;
import net.minecraft.block.Oxidizable;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;

public class CopperUtils {
    public static BlockState tryDegrade(Oxidizable oxidizable, BlockState state) {
        var result = oxidizable.getDegradationResult(state);
        return result.orElse(state);
    }

    public static boolean isWaterNearby(BlockPos pos, WorldAccess world) {
        BlockPos.Mutable mutable = pos.mutableCopy();
        Direction[] directions = Direction.values();

        BlockState blockState = world.getBlockState(mutable);

        //if it is water logged
        if (blockState.getFluidState().isOf(Fluids.WATER))
            return true;

        //should be surrounded by water
        int sideCount = 0;
        for (Direction direction : directions) {
            mutable.set(pos, direction);
            blockState = world.getBlockState(mutable);

            if (blockState.getFluidState().isOf(Fluids.WATER))
                sideCount++;
        }

        return sideCount >= 4;
    }
}
