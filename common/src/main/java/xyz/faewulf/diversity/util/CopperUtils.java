package xyz.faewulf.diversity.util;

import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;

public class CopperUtils {
    public static BlockState tryDegrade(WeatheringCopper oxidizable, BlockState state) {
        var result = oxidizable.getNext(state);
        return result.orElse(state);
    }

    public static boolean isWaterNearby(BlockPos pos, LevelAccessor world) {
        BlockPos.MutableBlockPos mutable = pos.mutable();
        Direction[] directions = Direction.values();

        BlockState blockState = world.getBlockState(mutable);

        //if it is water logged
        if (blockState.getFluidState().is(Fluids.WATER))
            return true;

        //should be surrounded by water
        int sideCount = 0;
        for (Direction direction : directions) {
            mutable.setWithOffset(pos, direction);
            blockState = world.getBlockState(mutable);

            if (blockState.getFluidState().is(Fluids.WATER))
                sideCount++;
        }

        return sideCount >= 4;
    }
}
